package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.entity.SysRoleResource;
import com.health.remind.mapper.SysRoleResourceMapper;
import com.health.remind.service.SysRoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色资源关联表 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements SysRoleResourceService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleResource(List<SysRoleResource> list) {
        list.stream()
                .findAny()
                .ifPresent(e -> {
                    Long sysRoleId = e.getSysRoleId();
                    remove(Wrappers.lambdaQuery(SysRoleResource.class)
                            .eq(SysRoleResource::getSysRoleId, sysRoleId));
                });
        return saveBatch(list);
    }

    @Override
    public List<Long> listRoleResourceByRoleId(Long roleId) {
        return baseMapper.selectListRoleResourceByRoleId(roleId);
    }
}
