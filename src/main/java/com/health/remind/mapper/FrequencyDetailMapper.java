package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.FrequencyDetail;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 频次详情表(时间明细表) Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Mapper
public interface FrequencyDetailMapper extends BaseMapper<FrequencyDetail> {

    Page<FrequencyDetailVO> selectPageFrequencyDetail(Page<FrequencyDetail> page,
                                                      @Param(Constants.WRAPPER) LambdaQueryWrapper<FrequencyDetail> wrapper);
}
