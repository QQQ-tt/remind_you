package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.SysUser;
import com.health.remind.mapper.SysUserMapper;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.util.NumUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
