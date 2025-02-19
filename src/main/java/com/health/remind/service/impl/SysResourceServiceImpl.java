package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.SysResource;
import com.health.remind.mapper.SysResourceMapper;
import com.health.remind.pojo.dto.SysResourceDTO;
import com.health.remind.pojo.dto.SysResourcePageDTO;
import com.health.remind.pojo.vo.SysResourceVO;
import com.health.remind.service.SysResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return baseMapper.selectPageResource(dto.getPage(),
                Wrappers.lambdaQuery(SysResource.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(SysResource::getParentId, 0)
                        .like(StringUtils.isNotBlank(dto.getName()), SysResource::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public List<SysResource> listResourceByParentId(Long id) {
        return list(Wrappers.lambdaQuery(SysResource.class)
                .eq(SysResource::getParentId, id));
    }

    @Override
    public boolean saveOrUpdateResource(SysResourceDTO dto) {
        long count = count(Wrappers.lambdaQuery(SysResource.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .eq(StringUtils.isNotBlank(dto.getName()), SysResource::getName, dto.getName()));
        if (count == 0) {
            SysResource sysResource = SysResource.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .status(dto.getStatus())
                    .type(dto.getType())
                    .method(dto.getMethod())
                    .url(dto.getUrl())
                    .parentId(dto.getParentId())
                    .description(dto.getDescription())
                    .build();
            return saveOrUpdate(sysResource);
        }
        throw new DataException(DataEnums.DATA_IS_ABNORMAL, "资源名称重复");
    }

    @Override
    public SysResource getResourceById(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateStatus(Long id, Boolean status) {
        return updateById(SysResource.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public boolean removeResource(Long id) {
        return removeById(id);
    }
}
