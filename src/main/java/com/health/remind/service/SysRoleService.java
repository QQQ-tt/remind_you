package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.SysRole;
import com.health.remind.pojo.dto.SysRoleDTO;
import com.health.remind.pojo.dto.SysRolePageDTO;
import com.health.remind.pojo.vo.SysRoleVO;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    Page<SysRoleVO> pageSysRole(SysRolePageDTO dto);

    /**
     * 保存或更新角色
     *
     * @param dto 角色详情
     * @return 是否保存成功
     */
    boolean saveOrUpdateSysRole(SysRoleDTO dto);

    /**
     * 查询所有角色
     *
     * @param name 角色名
     * @return 角色列表
     */
    List<SysRoleVO> listSysRole(String name);

    /**
     * 根据角色id查询角色
     *
     * @param id 角色id
     * @return 角色
     */
    SysRole getSysRoleById(Long id);

    /**
     * 更新角色状态
     *
     * @param id     角色id
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Boolean status);

    /**
     * 根据角色id删除角色
     *
     * @param id 角色id
     * @return 是否删除成功
     */
    boolean removeByRoleId(Long id);
}
