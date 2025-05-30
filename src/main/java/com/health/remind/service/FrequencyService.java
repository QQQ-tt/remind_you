package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.entity.Frequency;
import com.health.remind.pojo.dto.FrequencyDTO;
import com.health.remind.pojo.dto.FrequencyPageDTO;
import com.health.remind.pojo.vo.FrequencyVO;

import java.util.List;

/**
 * <p>
 * 频率 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
public interface FrequencyService extends IService<Frequency> {

    /**
     * 获取频率分页信息
     *
     * @param dto 分页参数
     * @return 频次分页信息
     */
    Page<FrequencyVO> pageFrequency(FrequencyPageDTO dto);

    /**
     * 获取所有频率信息
     *
     * @return 所有频率信息
     */
    List<FrequencyVO> listFrequency();

    /**
     * 获取频率信息
     *
     * @param id 频次id
     * @return 频率信息
     */
    FrequencyVO getFrequency(Long id);

    /**
     * 保存或更新频率信息
     *
     * @param dto 频率信息
     * @return 是否保存成功
     */
    boolean saveOrUpdateFrequency(FrequencyDTO dto);

    /**
     * 更新频率类型
     *
     * @param id   频率id
     * @param type 类型
     * @return 更新是否成功
     */
    boolean updateType(Long id, FrequencyTypeEnum type);

    /**
     * 更新频率状态
     *
     * @param id     频率id
     * @param status 状态
     * @return 更新是否成功
     */
    boolean updateStatus(Long id, boolean status);

    /**
     * 删除频率信息
     *
     * @param id 频次id
     * @return 是否删除成功
     */
    boolean removeFrequencyById(Long id);

    /**
     * 删除频率信息
     *
     * @param id 频次id
     * @return 是否删除成功
     */
    boolean removeAppFrequencyById(Long id);
}
