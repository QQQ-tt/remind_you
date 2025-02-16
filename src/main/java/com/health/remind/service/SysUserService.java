package com.health.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.SysUser;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 注册用户
     * @param signDTO 注册信息
     * @return 注册结果
     */
    SignVO signUser(SignDTO signDTO);

    /**
     * 登录用户
     *
     * @param account 账户
     * @param password 密码
     * @return 登录结果
     */
    LoginVO loginUser(Long account, String password);
}
