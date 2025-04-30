package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.SysUser;
import com.health.remind.mapper.SysUserMapper;
import com.health.remind.pojo.dto.AppUserDTO;
import com.health.remind.pojo.dto.LoginAppDTO;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.dto.SysUserDTO;
import com.health.remind.pojo.dto.SysUserPageDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.pojo.vo.SysUserVO;
import com.health.remind.service.SysRoleService;
import com.health.remind.service.SysUserService;
import com.health.remind.util.JwtUtils;
import com.health.remind.util.NumUtils;
import com.health.remind.util.RedisUtils;
import com.health.remind.wx.WxApiService;
import com.health.remind.wx.entity.Code2Session;
import com.health.remind.wx.entity.WXUserInfo;
import org.apache.commons.math3.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    private static final String USER_TYPE_SYS = "sys";

    private static final String USER_TYPE_APP = "app";

    private final PasswordEncoder passwordEncoder;

    private final SysRoleService sysRoleService;

    private final WxApiService wxApiService;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder, SysRoleService sysRoleService, WxApiService wxApiService) {
        this.passwordEncoder = passwordEncoder;
        this.sysRoleService = sysRoleService;
        this.wxApiService = wxApiService;
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
                .userType(USER_TYPE_SYS)
                .status(false)
                .build());
        return new SignVO(l);
    }

    @RedisLock(lockParameter = "#account", autoUnlockTime = 6000)
    @Override
    public LoginVO loginUser(Long account, String password) {
        SysUser sysUser = Optional.ofNullable(getOne(Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getUserType, USER_TYPE_SYS)
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
                    .userType(USER_TYPE_APP)
                    .openId(code2Session.getOpenid())
                    .userInfo(decrypt.getFirst())
                    .status(true)
                    .authorized(1)
                    .build();
            save(user);
            one = user;
        }
        LoginVO loginVO = getLoginVO(one);
        loginVO.setAuthorized(one.getAuthorized() == 1);
        return loginVO;
    }

    /**
     * 获取登录信息
     *
     * @param sysUser 用户信息
     * @return token
     */
    private LoginVO getLoginVO(SysUser sysUser) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(StaticConstant.USER_TYPE, sysUser.getUserType());
        // 设置角色id
        Optional.ofNullable(sysUser.getSysRoleId())
                .flatMap(e -> Optional.ofNullable(sysRoleService.getById(e)))
                .ifPresent(byId -> map.put(StaticConstant.ROLE_ID, byId.getStatus() ? byId.getId() : ""));
        // 生成token
        String s = JwtUtils.generateToken(sysUser.getAccount()
                .toString(), map);
        LoginVO loginVO = new LoginVO(sysUser.getId(), sysUser.getName(), s, JwtUtils.EXPIRATION_TIME);
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
                    .userType(USER_TYPE_SYS)
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
            return saveOrUpdate(sysUser);
        }
        throw new DataException(DataEnums.DATA_REPEAT, "手机号重复");
    }

    @Override
    public boolean updateAppUser(AppUserDTO dto) {
        return update(Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount())
                .eq(SysUser::getUserType, USER_TYPE_APP)
                .set(StringUtils.isNotBlank(dto.getName()), SysUser::getName, dto.getName())
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
}
