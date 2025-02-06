package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.RemindTask;
import com.health.remind.pojo.vo.RemindTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 提醒任务 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Mapper
public interface RemindTaskMapper extends BaseMapper<RemindTask> {

    Page<RemindTaskVO> selectPageTask(Page<RemindTask> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<RemindTask> wrapper);

    RemindTaskVO selectOneById(@Param("id") Long id,@Param("userId") Long userId);
}
