package com.health.remind.service;

import com.health.remind.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.pojo.dto.SignDTO;
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
     * @param signDTO
     * @return
     */
    SignVO signUser(SignDTO signDTO);

}
