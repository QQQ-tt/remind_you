package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.SysUser;
import com.health.remind.mail.MailService;
import com.health.remind.mapper.SysUserMapper;
import com.health.remind.pojo.dto.AppUserDTO;
import com.health.remind.pojo.dto.LoginAppDTO;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.dto.SysUserDTO;
import com.health.remind.pojo.dto.SysUserPageDTO;
import com.health.remind.pojo.dto.SysUserRulePageDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.pojo.vo.SysUserRuleVO;
import com.health.remind.pojo.vo.SysUserVO;
import com.health.remind.scheduler.DelaySqlScheduledExecutor;
import com.health.remind.service.RuleUserService;
import com.health.remind.service.SysRoleService;
import com.health.remind.service.SysUserService;
import com.health.remind.util.JwtUtils;
import com.health.remind.util.NumUtils;
import com.health.remind.util.RedisUtils;
import com.health.remind.wx.WxApiService;
import com.health.remind.wx.entity.Code2Session;
import com.health.remind.wx.entity.MsgInfo;
import com.health.remind.wx.entity.WXUserInfo;
import com.health.remind.wx.entity.WxMsg;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    private final SysRoleService sysRoleService;

    private final WxApiService wxApiService;

    private final RuleUserService ruleUserService;

    private final ScheduledExecutorService scheduler;

    private final ObjectMapper objectMapper;

    private final MailService mailService;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder, SysRoleService sysRoleService, WxApiService wxApiService, RuleUserService ruleUserService, ScheduledExecutorService scheduler, ObjectMapper objectMapper, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.sysRoleService = sysRoleService;
        this.wxApiService = wxApiService;
        this.ruleUserService = ruleUserService;
        this.scheduler = scheduler;
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @RedisLock(lockParameter = "T(com.health.remind.config.CommonMethod).getIp()", autoUnlockTime = 6000)
    @Override
    public SignVO signUser(SignDTO signDTO) {
        String password = signDTO.getPassword();
        String againPassword = signDTO.getAgainPassword();
        if (!password.equals(againPassword)) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL, "两次密码不一致");
        }
        long count = count(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getTelephone, signDTO.getTelephone()));
        if (count > 0) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL, "手机号已存在");
        }
        long l = getAccount();
        String encode = passwordEncoder.encode(signDTO.getPassword());
        save(SysUser.builder()
                .name(signDTO.getName())
                .account(l)
                .password(encode)
                .telephone(Long.valueOf(signDTO.getTelephone()))
                .encryptedTelephone(signDTO.getTelephone()
                        .replaceFirst("(\\d{3})\\d{4}(\\d{4})", "$1****$2"))
                .userType(StaticConstant.USER_TYPE_SYS)
                .status(false)
                .build());
        return new SignVO(l);
    }

    @RedisLock(lockParameter = "#account", autoUnlockTime = 6000)
    @Override
    public LoginVO loginUser(Long account, String password) {
        SysUser sysUser = Optional.ofNullable(getOne(Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getUserType, StaticConstant.USER_TYPE_SYS)
                        .and(e -> e.eq(SysUser::getAccount, account)
                                .or()
                                .eq(SysUser::getTelephone, account))))
                .orElseThrow(() -> new DataException(DataEnums.DATA_IS_ABNORMAL, "用户不存在"));
        if (!sysUser.getStatus()) {
            throw new DataException(DataEnums.USER_STATUS_ERROR);
        }
        if (passwordEncoder.matches(password, sysUser.getPassword())) {
            return getLoginVO(sysUser);
        }
        throw new DataException(DataEnums.PASSWORD_ERROR);
    }

    @Override
    public LoginVO loginAppUser(LoginAppDTO dto) {
        Code2Session code2Session = wxApiService.getCode2Session(dto.getCode());
        SysUser one = getOne(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getStatus, true)
                .eq(SysUser::getOpenId, code2Session.getOpenid()));
        if (one == null) {
            Pair<String, WXUserInfo> decrypt = wxApiService.decrypt(dto.getEncryptedData(),
                    code2Session.getSession_key(),
                    dto.getIv());
            long account = getAccount();
            SysUser user = SysUser.builder()
                    .name(decrypt.getSecond()
                            .getNickName())
                    .account(account)
                    .password(passwordEncoder.encode(String.valueOf(account)))
                    .userType(StaticConstant.USER_TYPE_APP)
                    .openId(code2Session.getOpenid())
                    .userInfo(decrypt.getFirst())
                    .status(true)
                    .authorized(1)
                    .sysRoleId(1901928991601274882L)
                    .interestsLevel(InterestsLevelEnum.VIP_0)
                    .sharedUserId(dto.getSharedUserId())
                    .build();
            save(user);
            one = user;
            CommonMethod.setAccount(String.valueOf(one.getAccount()));
            ruleUserService.saveRuleByUser(one);
        }
        LoginVO loginVO = getLoginVO(one);
        loginVO.setAuthorized(one.getAuthorized() == 1);
        loginVO.setAvatar(one.getAvatar());
        return loginVO;
    }

    @Override
    @RedisLock(lockParameter = "#token", autoUnlockTime = 6000)
    public LoginVO refreshToken(String token) {
        AtomicReference<LoginVO> vo = new AtomicReference<>();
        Optional<String> bodyFromToken = Optional.ofNullable(JwtUtils.getBodyFromToken(token));
        bodyFromToken.ifPresentOrElse(e -> {
            LoginVO loginVO = getLoginVO(getOne(Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getAccount, e)));
            vo.set(loginVO);
        }, () -> {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL, "token异常");
        });
        return vo.get();
    }

    @Override
    public Integer getMsgNum() {
        SysUser one = getOne(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount()));
        return one == null ? 0 : one.getMsgNum();
    }

    @Override
    public Integer increasePushCount() {
        boolean update = update(Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount())
                .setSql("msg_num = msg_num + 1"));
        if (update) {
            return getOne(Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getAccount, CommonMethod.getAccount())).getMsgNum();
        }
        return 0;
    }

    @Override
    @RedisLock(lockParameter = "T(com.health.remind.config.CommonMethod).getAccount()", autoUnlockTime = 6000)
    public Integer testMsg() {
        SysUser one = getOne(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount()));
        Integer msgNum = one.getMsgNum();
        if (msgNum > 0) {
            update(Wrappers.lambdaUpdate(SysUser.class)
                    .eq(SysUser::getAccount, CommonMethod.getAccount())
                    .setSql("msg_num = msg_num - 1"));
            WxMsg wxMsg = new WxMsg();
            wxMsg.setTemplate_id("ahi62RYx-WStwOclzC26ZEBaSgZNaVWjs_eyuFefWzM");
            wxMsg.setTouser(one.getOpenId());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            wxMsg.setData(
                    Map.of("thing7", new MsgInfo("测试消息"), "thing9", new MsgInfo("备注123"), "time11",
                            new MsgInfo(pattern.format(now)), "date6",
                            new MsgInfo(LocalDate.now()
                                    .toString())));
            wxApiService.sendMsg(wxMsg);
            return msgNum - 1;
        }
        return 0;
    }

    /**
     * 获取登录信息
     *
     * @param sysUser 用户信息
     * @return token
     */
    @SneakyThrows
    private LoginVO getLoginVO(SysUser sysUser) {
        setLoginTime(sysUser);
        HashMap<String, Object> map = new HashMap<>();
        map.put(StaticConstant.USER_ID, sysUser.getId());
        map.put(StaticConstant.USER_TYPE, sysUser.getUserType());
        map.put(StaticConstant.INTERESTS_LEVEL, sysUser.getInterestsLevel());
        // 设置角色id
        Optional.ofNullable(sysUser.getSysRoleId())
                .flatMap(e -> Optional.ofNullable(sysRoleService.getById(e)))
                .ifPresent(byId -> map.put(StaticConstant.ROLE_ID, byId.getStatus() ? byId.getId() : ""));
        // 生成token
        String s = JwtUtils.generateToken(sysUser.getAccount()
                .toString(), map);
        LoginVO loginVO = new LoginVO(sysUser.getId(), sysUser.getName(), sysUser.getMsgNum(), s,
                JwtUtils.EXPIRATION_TIME);
        RedisUtils.delete(RedisKeys.getLoginKey(sysUser.getAccount()
                .toString(), sysUser.getUserType()));
        Optional.ofNullable(sysUser.getTelephone())
                .ifPresent(e -> RedisUtils.setObject(RedisKeys.getLoginKey(e
                        .toString(), sysUser.getUserType()), loginVO));
        RedisUtils.setObject(RedisKeys.getLoginKey(sysUser.getAccount()
                .toString(), sysUser.getUserType()), loginVO);
        RedisUtils.expire(RedisKeys.getLoginKey(sysUser.getAccount()
                        .toString(), sysUser.getUserType()),
                JwtUtils.EXPIRATION_TIME,
                TimeUnit.MILLISECONDS);

        RedisUtils.set(RedisKeys.getUserKey(sysUser.getAccount(), sysUser.getUserType()),
                objectMapper.writeValueAsString(sysUser));
        RedisUtils.persist(RedisKeys.getUserKey(sysUser.getAccount(), sysUser.getUserType()));
        return loginVO;
    }

    @Override
    public Page<SysUserVO> pageSysUser(SysUserPageDTO dto) {
        return baseMapper.selectPageSysUser(dto.getPage(),
                Wrappers.lambdaQuery(SysUser.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(SysUser::getUserType, dto.getUserType())
                        .eq(dto.getStatus() != null, SysUser::getStatus, dto.getStatus())
                        .like(StringUtils.isNotBlank(dto.getName()), SysUser::getName,
                                dto.getName())
                        .like(dto.getAccount() != null, SysUser::getAccount, dto.getAccount())
                        .like(StringUtils.isNotBlank(dto.getTelephone()), SysUser::getTelephone, dto.getTelephone()));
    }

    @Override
    public Page<SysUserRuleVO> pageSysUserRule(SysUserRulePageDTO dto) {
        return baseMapper.selectPageSysUserRule(dto.getPage(),
                Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getUserType, StaticConstant.USER_TYPE_APP)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .ge(SysUser::getLoginTime, LocalDateTime.now()
                                .minusMonths(1))
                        .like(StringUtils.isNotBlank(dto.getName()), SysUser::getName, dto.getName())
                        .eq(dto.getInterestsLevel() != null, SysUser::getInterestsLevel, dto.getInterestsLevel())
        );
    }

    @Override
    public boolean saveOrUpdateSysUser(SysUserDTO dto) {
        boolean b = dto.getId() == null;
        long count = 0;
        if (StringUtils.isNotBlank(dto.getTelephone())) {
            count = count(Wrappers.lambdaQuery(SysUser.class)
                    .ne(!b, BaseEntity::getId, dto.getId())
                    .eq(SysUser::getTelephone, dto.getTelephone()));
        }
        if (count == 0) {
            SysUser sysUser = SysUser.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .status(dto.isStatus())
                    .userType(dto.getUserType())
                    .sysRoleId(dto.getSysRoleId())
                    .build();
            if (b && StringUtils.isNotBlank(dto.getPassword())) {
                sysUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            if (b) {
                sysUser.setAccount(getAccount());
                sysUser.setTelephone(Long.valueOf(dto.getTelephone()));
                sysUser.setEncryptedTelephone(dto.getTelephone()
                        .replaceFirst("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            if (!b && !dto.isStatus()) {
                SysUser byId = getById(dto.getId());
                String loginKey = RedisKeys.getLoginKey(String.valueOf(byId.getAccount()), byId.getUserType());
                RedisUtils.delete(loginKey);
            }
            setRedis(sysUser.getAccount());
            return saveOrUpdate(sysUser);
        }
        throw new DataException(DataEnums.DATA_REPEAT, "手机号重复");
    }

    @Override
    public boolean updateEmail(String email, String code) {
        String emailCode = RedisKeys.getEmailCode(StaticConstant.EMAIL_CODE_USER, CommonMethod.getAccount(),
                email);
        String s = RedisUtils.get(emailCode);
        if (StringUtils.isNotBlank(s) && s.equals(code)) {
            return update(Wrappers.lambdaUpdate(SysUser.class)
                    .eq(SysUser::getAccount, CommonMethod.getAccount())
                    .set(SysUser::getEmail, email));
        }
        return false;
    }

    @Override
    @RedisLock(lockParameter = "T(com.health.remind.config.CommonMethod).getAccount()")
    public void sendEmailCode(String email) {
        mailService.sendCode(StaticConstant.EMAIL_CODE_USER, email);
    }

    @Override
    public boolean updateAppUser(AppUserDTO dto) {
        return update(Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount())
                .eq(SysUser::getUserType, StaticConstant.USER_TYPE_APP)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateId, CommonMethod.getAccount())
                .set(BaseEntity::getUpdateName, CommonMethod.getUserName())
                .set(StringUtils.isNotBlank(dto.getNickname()), SysUser::getName, dto.getNickname())
                .set(StringUtils.isNotBlank(dto.getAvatar()), SysUser::getAvatar, dto.getAvatar())
                .set(SysUser::getAuthorized, dto.getAuthorized()));
    }

    @Override
    public boolean cancelRole(Long id) {
        return update(Wrappers.lambdaUpdate(SysUser.class)
                .eq(BaseEntity::getId, id)
                .set(SysUser::getSysRoleId, null));
    }


    @Override
    public SysUser getSysUserById(Long id) {
        return getById(id);
    }

    @Override
    public SysUser getAppSysUserByAccount(Long account) {
        String userKey = RedisKeys.getUserKey(account, StaticConstant.USER_TYPE_APP);
        SysUser object = RedisUtils.getObject(userKey, new TypeReference<>() {
        });
        if (object != null) {
            return object;
        }
        object = getOne(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAccount, account)
                .eq(SysUser::getUserType, StaticConstant.USER_TYPE_APP));
        return object;
    }

    @Override
    public boolean removeByUserId(Long id) {
        return removeById(id);
    }

    @Override
    public boolean testToken() {
        return true;
    }

    private long getAccount() {
        long l;
        do {
            l = NumUtils.numUserCard();
        } while (count(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAccount, l)) > 0);
        return l;
    }

    @SneakyThrows
    private void setLoginTime(SysUser sysUser) {
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setLoginTime(sysUser.getLoginTime());
        DelaySqlScheduledExecutor.putDelaySqlTask(this, Collections.singletonList(user), SysUser.class);
    }

    public void setRedis(Long account) {
        scheduler.schedule(() -> {
            SysUser one = getOne(Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getAccount, account));
            try {
                RedisUtils.set(RedisKeys.getUserKey(one.getAccount(), one.getUserType()),
                        objectMapper.writeValueAsString(one));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, 5, TimeUnit.SECONDS);

    }
}
