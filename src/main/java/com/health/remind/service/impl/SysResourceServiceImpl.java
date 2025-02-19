package com.health.remind.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.SysResource;
import com.health.remind.mapper.SysResourceMapper;
import com.health.remind.pojo.dto.SysResourceDTO;
import com.health.remind.pojo.dto.SysResourcePageDTO;
import com.health.remind.pojo.vo.SysResourceVO;
import com.health.remind.service.SysResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统资源 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    @Override
    public Page<SysResourceVO> pageResource(SysResourcePageDTO dto) {
        return null;
    }

    @Override
    public boolean saveOrUpdateResource(SysResourceDTO resource) {
        return false;
    }

    @Override
    public SysResource getResourceById(Long id) {
        return null;
    }

    @Override
    public boolean updateStatus(Long id, Boolean status) {
        return false;
    }

    @Override
    public boolean removeResource(Long id) {
        return false;
    }
}
