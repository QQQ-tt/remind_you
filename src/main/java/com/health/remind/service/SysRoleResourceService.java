package com.health.remind.service;

import com.health.remind.entity.SysRoleResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色资源关联表 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
public interface SysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 保存角色资源关联
     *
     * @param list 角色资源关联
     * @return 是否成功
     */
    boolean saveRoleResource(List<SysRoleResource> list);

    /**
     * 根据角色id查询角色资源关联
     *
     * @param roleId 角色id
     * @return 角色资源集合
     */
    List<Long> listRoleResourceByRoleId(Long roleId);
}
