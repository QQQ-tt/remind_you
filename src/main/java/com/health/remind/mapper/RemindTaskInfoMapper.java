package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务执行详情数据 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-27
 */
@Mapper
public interface RemindTaskInfoMapper extends BaseMapper<RemindTaskInfo> {

    Integer selectTaskError();

    Page<RemindTaskInfoVO> selectPageTaskInfo(Page<RemindTaskInfo> page,
                                              @Param(Constants.WRAPPER) LambdaQueryWrapper<RemindTaskInfo> eq);

    List<RemindTaskInfoVO> selectTaskInfo(@Param(Constants.WRAPPER) LambdaQueryWrapper<RemindTaskInfo> eq);
}
