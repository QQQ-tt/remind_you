package com.health.remind.mapper;

import com.health.remind.entity.SysRoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色资源关联表 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {

    List<Long> selectListRoleResourceByRoleId(@Param("roleId") Long roleId);
}
