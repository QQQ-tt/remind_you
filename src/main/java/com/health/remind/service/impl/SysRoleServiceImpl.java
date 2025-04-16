package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.SysRole;
import com.health.remind.entity.SysRoleResource;
import com.health.remind.mapper.SysRoleMapper;
import com.health.remind.pojo.dto.SysRoleDTO;
import com.health.remind.pojo.dto.SysRolePageDTO;
import com.health.remind.pojo.vo.SysRoleVO;
import com.health.remind.service.SysRoleResourceService;
import com.health.remind.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleResourceService sysRoleResourceService;

    public SysRoleServiceImpl(SysRoleResourceService sysRoleResourceService) {
        this.sysRoleResourceService = sysRoleResourceService;
    }

    @Override
    public Page<SysRoleVO> pageSysRole(SysRolePageDTO dto) {
        return baseMapper.selectPageSysRole(dto.getPage(),
                Wrappers.lambdaQuery(SysRole.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(dto.getStatus() != null, SysRole::getStatus, dto.getStatus())
                        .like(StringUtils.isNotBlank(dto.getName()), SysRole::getName, dto.getName())
                        .orderByDesc(BaseEntity::getUpdateTime)
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public boolean saveOrUpdateSysRole(SysRoleDTO dto) {
        long count = count(Wrappers.lambdaQuery(SysRole.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .eq(StringUtils.isNotBlank(dto.getName()), SysRole::getName, dto.getName()));
        if (count == 0) {
            SysRole sysRole = SysRole.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .status(dto.getStatus())
                    .remark(dto.getRemark())
                    .build();
            return saveOrUpdate(sysRole);
        }
        throw new DataException(DataEnums.DATA_IS_ABNORMAL, "角色名称重复");
    }

    @Override
    public List<SysRoleVO> listSysRole(String name) {
        return list(Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getStatus, true)
                .like(StringUtils.isNotBlank(name), SysRole::getName, name)).stream()
                .map(m -> SysRoleVO.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .build())
                .toList();
    }

    @Override
    public SysRole getSysRoleById(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateStatus(Long id, Boolean status) {
        return updateById(SysRole.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public boolean removeByRoleId(Long id) {
        sysRoleResourceService.remove(Wrappers.lambdaQuery(SysRoleResource.class)
                .eq(SysRoleResource::getSysRoleId, id));
        return removeById(id);
    }
}
