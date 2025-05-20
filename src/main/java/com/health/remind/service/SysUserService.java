package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.SysUser;
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

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 注册用户
     *
     * @param signDTO 注册信息
     * @return 注册结果
     */
    SignVO signUser(SignDTO signDTO);

    /**
     * 登录用户
     *
     * @param account  账户
     * @param password 密码
     * @return 登录结果
     */
    LoginVO loginUser(Long account, String password);

    /**
     * 微信登录
     *
     * @param dto 微信信息
     * @return 登录结果
     */
    LoginVO loginAppUser(LoginAppDTO dto);

    /**
     * 刷新token
     *
     * @param token token
     * @return 登录结果
     */
    LoginVO refreshToken(String token);

    /**
     * 添加消息次数
     *
     * @return 添加结果
     */
    Integer addMsg();

    /**
     * 测试消息发送
     *
     * @return 消息次数
     */
    Integer testMsg();

    /**
     * 分页查询用户
     *
     * @param dto 分页信息
     * @return 用户集合
     */
    Page<SysUserVO> pageSysUser(SysUserPageDTO dto);

    /**
     * 分页查询用户权益
     *
     * @param dto 分页信息
     * @return app用户集合
     */
    Page<SysUserRuleVO> pageSysUserRule(SysUserRulePageDTO dto);

    /**
     * 用户创建和编辑
     *
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean saveOrUpdateSysUser(SysUserDTO dto);

    /**
     * 编辑用户
     *
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean updateAppUser(AppUserDTO dto);

    /**
     * 取消角色
     *
     * @param id 用户id
     * @return 是否成功
     */
    boolean cancelRole(Long id);

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    SysUser getSysUserById(Long id);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否成功
     */
    boolean removeByUserId(Long id);

    /**
     * 测试token
     *
     * @return true or false
     */
    boolean testToken();
}
