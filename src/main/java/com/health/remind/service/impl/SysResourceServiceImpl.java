package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.enums.SysResourceEnum;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Page<SysResourceVO> page = baseMapper.selectPageResource(dto.getPage(),
                Wrappers.lambdaQuery(SysResource.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(StringUtils.isBlank(dto.getName()),SysResource::getParentId, 0)
                        .like(StringUtils.isNotBlank(dto.getName()), SysResource::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
        List<SysResourceVO> list = list(Wrappers.lambdaQuery(SysResource.class)).stream().map(e-> SysResourceVO.builder()
                .id(e.getId())
                .name(e.getName())
                .url(e.getUrl())
                .type(e.getType())
                .method(e.getMethod())
                .description(e.getDescription())
                .status(e.getStatus())
                .createTime(e.getCreateTime())
                .updateTime(e.getUpdateTime())
                .parentId(e.getParentId())
                .build()).toList();
        Map<Long, List<SysResourceVO>> hashMap = new HashMap<>();
        list.forEach(e -> hashMap.computeIfAbsent(e.getParentId(), k -> new ArrayList<>())
                .add(e));
        page.getRecords().forEach(e -> setChildren(e, hashMap));
        return page;
    }

    @Override
    public List<SysResource> listResourceByParentId(Long id) {
        return list(Wrappers.lambdaQuery(SysResource.class)
                .eq(SysResource::getParentId, id));
    }

    @Override
    public List<SysResource> treeResource(SysResourceEnum type) {
        List<SysResource> list = list(Wrappers.lambdaQuery(SysResource.class)
                .eq(SysResource::getStatus, true)
                .eq(type != null, SysResource::getType, type));
        // 树结构优化构造
        Map<Long, List<SysResource>> hashMap = new HashMap<>();
        list.forEach(e -> hashMap.computeIfAbsent(e.getParentId(), k -> new ArrayList<>())
                .add(e));
        return list.stream()
                .filter(f -> f.getParentId() == 0)
                .peek(e -> setChildren(e, hashMap))
                .toList();
    }

    private void setChildren(SysResource sysResource, Map<Long, List<SysResource>> hashMap) {
        Optional.ofNullable(hashMap.get(sysResource.getId()))
                .ifPresent(i -> {
                    sysResource.setChildren(i);
                    i.forEach(e -> setChildren(e, hashMap));
                });
    }

    private void setChildren(SysResourceVO sysResource, Map<Long, List<SysResourceVO>> hashMap) {
        Optional.ofNullable(hashMap.get(sysResource.getId()))
                .ifPresent(i -> {
                    sysResource.setChildren(i);
                    i.forEach(e -> setChildren(e, hashMap));
                });
    }

    @Override
    public boolean saveOrUpdateResource(SysResourceDTO dto) {
        long count = count(Wrappers.lambdaQuery(SysResource.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .eq(SysResource::getParentId, dto.getParentId())
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
