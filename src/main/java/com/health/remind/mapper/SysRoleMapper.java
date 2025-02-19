package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.remind.pojo.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Page<SysRoleVO> selectPageSysRole(Page<SysRole> page,
                                      @Param(Constants.WRAPPER) LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper);
}
