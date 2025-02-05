package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.FrequencyDetail;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.FrequencyDetailPageDTO;
import com.health.remind.pojo.vo.FrequencyDetailVO;

/**
 * <p>
 * 频次详情表(时间明细表) 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
public interface FrequencyDetailService extends IService<FrequencyDetail> {

    /**
     * 分页查询频次详情
     *
     * @param dto 查询条件
     * @return 分页集合
     */
    Page<FrequencyDetailVO> pageFrequencyDetail(FrequencyDetailPageDTO dto);

    /**
     * 新增或修改频次详情
     *
     * @param frequencyDetail 频次详情
     * @return 是否成功
     */
    boolean saveOrUpdateFrequencyDetail(FrequencyDetailDTO frequencyDetail);

    /**
     * 删除频次详情
     *
     * @param id 频次详情id
     * @return 是否成功
     */
    boolean removeFrequencyDetail(Long id);

}
