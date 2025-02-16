package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.SysUser;
import com.health.remind.mapper.SysUserMapper;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.service.SysUserService;
import com.health.remind.util.JwtUtils;
import com.health.remind.util.NumUtils;
import com.health.remind.util.RedisUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignVO signUser(SignDTO signDTO) {
        String password = signDTO.getPassword();
        String againPassword = signDTO.getAgainPassword();
        if (!password.equals(againPassword)){
            throw new DataException(DataEnums.DATA_IS_ABNORMAL);
        }
        long l;
        do {
            l = NumUtils.numUserCard();
        } while (count(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getAccount, l)) > 0);

        String encode = passwordEncoder.encode(signDTO.getPassword());
        save(SysUser.builder()
                .name(signDTO.getName())
                .account(l)
                .password(encode)
                .telephone(signDTO.getTelephone())
                .build());
        return new SignVO(l);
    }

    @RedisLock(lockParameter = "#account", autoUnlockTime = 6000)
    @Override
    public LoginVO loginUser(Long account, String password) {
        SysUser sysUser = Optional.ofNullable(getOne(Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getAccount, account)))
                .orElseThrow(() -> new DataException(DataEnums.DATA_IS_ABNORMAL, "用户不存在"));
        if (passwordEncoder.matches(password, sysUser.getPassword())) {
            Long sysRoleId = sysUser.getSysRoleId();
            String s = JwtUtils.generateToken(sysUser.getId()
                    .toString(), Map.of("role", sysRoleId == null ? "0" : sysRoleId.toString()));
            LoginVO loginVO = new LoginVO(sysUser.getName(), s);
            RedisUtils.setObject(RedisKeys.getLoginKey(account.toString()), loginVO);
            RedisUtils.expire(RedisKeys.getLoginKey(account.toString()), JwtUtils.EXPIRATION_TIME,
                    TimeUnit.MILLISECONDS);
            return loginVO;
        }
        return null;
    }
}
