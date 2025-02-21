package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.remind.entity.SysRoleResource;
import com.health.remind.pojo.bo.SysRoleResourceBO;
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

    List<SysRoleResourceBO> selectUrlListByRoleIds(@Param("roleIds") List<Long> roleIds);
}
