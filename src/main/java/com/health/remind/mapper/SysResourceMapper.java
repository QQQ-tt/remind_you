package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.SysResource;
import com.health.remind.pojo.vo.SysResourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统资源 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    Page<SysResourceVO> selectPageResource(Page<SysResource> page,
                                           @Param(Constants.WRAPPER) LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper);
}
