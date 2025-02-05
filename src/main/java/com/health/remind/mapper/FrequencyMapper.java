package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.Frequency;
import com.health.remind.pojo.vo.FrequencyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 频率 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Mapper
public interface FrequencyMapper extends BaseMapper<Frequency> {

    Page<FrequencyVO> selectPageFrequency(Page<Frequency> page,
                                          @Param(Constants.WRAPPER) LambdaQueryWrapper<Frequency> wrapper);

    List<FrequencyVO> selectListFrequency(@Param(Constants.WRAPPER) LambdaQueryWrapper<Frequency> wrapper);
}
