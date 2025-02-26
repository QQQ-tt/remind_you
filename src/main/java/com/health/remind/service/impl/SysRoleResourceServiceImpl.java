package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.cache.JavaCache;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.entity.SysRoleResource;
import com.health.remind.mapper.SysRoleResourceMapper;
import com.health.remind.pojo.bo.SysRoleResourceBO;
import com.health.remind.pojo.dto.SysRoleResourceDTO;
import com.health.remind.service.SysRoleResourceService;
import com.health.remind.util.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final JavaCache cache;

    public SysRoleResourceServiceImpl(JavaCache cache) {
        this.cache = cache;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleResource(SysRoleResourceDTO dto) {
        Long sysRoleId = dto.getSysRoleId();
        remove(Wrappers.lambdaQuery(SysRoleResource.class)
                .eq(SysRoleResource::getSysRoleId, sysRoleId));
        Optional<List<Long>> sysResources = Optional.ofNullable(dto.getSysResources());
        sysResources.ifPresent(list -> {
            if (!list.isEmpty()) {
                saveBatch(list.stream()
                        .map(m -> {
                            SysRoleResource sysRoleResource = new SysRoleResource();
                            sysRoleResource.setSysRoleId(sysRoleId);
                            sysRoleResource.setSysResource(m);
                            return sysRoleResource;
                        })
                        .toList());
                listRoleResourceByRoleId(Collections.singletonList(sysRoleId));
            }
        });
        return sysResources.isPresent();
    }

    @Override
    public List<Long> listRoleResourceByRoleId(Long roleId) {
        return baseMapper.selectListRoleResourceByRoleId(roleId);
    }

    @Override
    public Map<Long, List<String>> listRoleResourceByRoleId(List<Long> roleIds) {
        List<SysRoleResourceBO> list = baseMapper.selectUrlListByRoleIds(roleIds);
        Map<Long, List<String>> listMap = list.stream()
                .collect(Collectors.groupingBy(SysRoleResourceBO::getSysRoleId,
                        Collectors.mapping(SysRoleResourceBO::getUrl, Collectors.toList())));
        listMap.forEach((k, v) -> {
            CommonMethod.TrieNode node = cache.getTrieNode(k, v);
            RedisUtils.delete(RedisKeys.getRoleResourceKey(k));
            RedisUtils.setObject(RedisKeys.getRoleResourceKey(k), node);
            RedisUtils.persist(RedisKeys.getRoleResourceKey(k));
        });
        return listMap;
    }
}
