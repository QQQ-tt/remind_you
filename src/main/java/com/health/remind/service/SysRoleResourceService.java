package com.health.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.SysRoleResource;
import com.health.remind.pojo.dto.SysRoleResourceDTO;

import java.util.List;
import java.util.Map;

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
     * @param dto 角色资源关联
     * @return 是否成功
     */
    boolean saveRoleResource(SysRoleResourceDTO dto);

    /**
     * 根据角色id查询角色资源关联
     *
     * @param roleId 角色id
     * @return 角色资源集合
     */
    List<Long> listRoleResourceByRoleId(Long roleId);

    /**
     * 根据角色id查询角色请求地址url资源
     *
     * @param roleIds 角色id集合
     * @return 资源集合
     */
    Map<Long, List<String>> listRoleResourceByRoleId(List<Long> roleIds);
}
