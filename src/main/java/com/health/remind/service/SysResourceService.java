package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.SysResource;
import com.health.remind.pojo.dto.SysResourceDTO;
import com.health.remind.pojo.dto.SysResourcePageDTO;
import com.health.remind.pojo.vo.SysResourceVO;

/**
 * <p>
 * 系统资源 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
public interface SysResourceService extends IService<SysResource> {

    /**
     * 分页查询资源信息
     *
     * @param dto 分页参数
     * @return 分页集合
     */
    Page<SysResourceVO> pageResource(SysResourcePageDTO dto);

    /**
     * 保存或更新资源信息
     *
     * @param resource 资源信息
     * @return 是否成功
     */
    boolean saveOrUpdateResource(SysResourceDTO resource);

    /**
     * 根据id查询资源信息
     *
     * @param id 资源id
     * @return 资源信息
     */
    SysResource getResourceById(Long id);

    /**
     * 更新资源状态
     *
     * @param id     资源id
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Boolean status);

    /**
     * 删除资源信息
     *
     * @param id 资源id
     * @return 是否成功
     */
    boolean removeResource(Long id);

}
