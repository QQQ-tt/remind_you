package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.SysUser;
import com.health.remind.mapper.SysUserMapper;
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

    private static final String USER_TYPE = "sys";

    private final PasswordEncoder passwordEncoder;

    private final SysRoleService sysRoleService;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder, SysRoleService sysRoleService) {
        this.passwordEncoder = passwordEncoder;
        this.sysRoleService = sysRoleService;
    }

    @Override
    public SignVO signUser(SignDTO signDTO) {
        String password = signDTO.getPassword();
        String againPassword = signDTO.getAgainPassword();
        if (!password.equals(againPassword)) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL);
        }
        long l = getAccount();
        String encode = passwordEncoder.encode(signDTO.getPassword());
        save(SysUser.builder()
                .name(signDTO.getName())
                .account(l)
                .password(encode)
                .telephone(Long.valueOf(signDTO.getTelephone()))
                .userType(USER_TYPE)
                .build());
        return new SignVO(l);
    }

    @RedisLock(lockParameter = "#account", autoUnlockTime = 6000)
    @Override
    public LoginVO loginUser(Long account, String password) {
        SysUser sysUser = Optional.ofNullable(getOne(Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getUserType, USER_TYPE)
                        .eq(SysUser::getStatus, true)
                        .and(e -> e.eq(SysUser::getAccount, account)
                                .or()
                                .eq(SysUser::getTelephone, account))))
                .orElseThrow(() -> new DataException(DataEnums.DATA_IS_ABNORMAL, "用户不存在"));
        if (passwordEncoder.matches(password, sysUser.getPassword())) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(StaticConstant.USER_TYPE, sysUser.getUserType());
            Optional.ofNullable(sysUser.getSysRoleId())
                    .flatMap(e -> Optional.ofNullable(sysRoleService.getById(e)))
                    .ifPresent(byId -> map.put(StaticConstant.ROLE_ID, byId.getStatus() ? byId.getId() : ""));
            String s = JwtUtils.generateToken(sysUser.getAccount()
                    .toString(), map);
            LoginVO loginVO = new LoginVO(sysUser.getId(), sysUser.getName(), s);
            RedisUtils.setObject(RedisKeys.getLoginKey(account.toString(), sysUser.getUserType()), loginVO);
            RedisUtils.expire(RedisKeys.getLoginKey(account.toString(), sysUser.getUserType()),
                    JwtUtils.EXPIRATION_TIME,
                    TimeUnit.MILLISECONDS);
            return loginVO;
        }
        throw new DataException(DataEnums.PASSWORD_ERROR);
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
                        .like(StringUtils.isNotBlank(dto.getTelephone()), SysUser::getTelephone, dto.getTelephone())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public boolean saveOrUpdateSysUser(SysUserDTO dto) {
        boolean b = dto.getId() != null;
        long count = count(Wrappers.lambdaQuery(SysUser.class)
                .ne(b, BaseEntity::getId, dto.getId())
                .eq(StringUtils.isNotBlank(dto.getTelephone()), SysUser::getTelephone, dto.getTelephone()));
        if (count == 0) {
            SysUser sysUser = SysUser.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .telephone(Long.valueOf(dto.getTelephone()))
                    .status(dto.isStatus())
                    .userType(USER_TYPE)
                    .build();
            if (!b) {
                sysUser.setAccount(getAccount());
            }
            saveOrUpdate(sysUser);
        }
        throw new DataException(DataEnums.DATA_REPEAT, "手机号重复");
    }

    @Override
    public SysUser getSysUserById(Long id) {
        return getById(id);
    }

    @Override
    public boolean removeByUserId(Long id) {
        return removeById(id);
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
