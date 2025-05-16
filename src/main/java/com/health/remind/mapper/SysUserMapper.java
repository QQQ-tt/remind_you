package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.SysUser;
import com.health.remind.pojo.vo.SysUserRuleVO;
import com.health.remind.pojo.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<SysUserVO> selectPageSysUser(Page<SysUser> page,
                                      @Param(Constants.WRAPPER) LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper);

    Page<SysUserRuleVO> selectPageSysUserRule(Page<SysUser> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<SysUser> eq);
}
