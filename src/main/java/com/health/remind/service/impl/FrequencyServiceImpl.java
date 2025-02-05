package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.Frequency;
import com.health.remind.mapper.FrequencyMapper;
import com.health.remind.pojo.dto.FrequencyDTO;
import com.health.remind.pojo.dto.FrequencyPageDTO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.FrequencyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 频率 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Service
public class FrequencyServiceImpl extends ServiceImpl<FrequencyMapper, Frequency> implements FrequencyService {

    @Override
    public Page<FrequencyVO> pageFrequency(FrequencyPageDTO dto) {
        return baseMapper.selectPageFrequency(dto.getPage(),
                Wrappers.lambdaQuery(Frequency.class)
                        .like(StringUtils.isNotBlank(dto.getFrequencyName()),
                                Frequency::getFrequencyName, dto.getFrequencyName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public List<FrequencyVO> listFrequency() {
        return baseMapper.selectListFrequency(Wrappers.lambdaQuery(Frequency.class)
                .le(Frequency::getLevel, getLevel())
                .eq(Frequency::getStatus, true)
                .in(Frequency::getSource, "sys",
                        CommonMethod.getUserId()));
    }

    @Override
    public boolean saveOrUpdateFrequency(FrequencyDTO dto) {
        boolean b = StringUtils.isNotBlank(dto.getFrequencyCode()) && StringUtils.isNotBlank(dto.getFrequencyName());
        if (!b) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL);
        }
        long count = count(Wrappers.lambdaQuery(Frequency.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .and(w -> w.eq(Frequency::getFrequencyName, dto.getFrequencyName())
                        .or()
                        .eq(Frequency::getFrequencyCode, dto.getFrequencyCode())));
        if (count == 0) {
            return saveOrUpdate(Frequency.builder()
                    .frequencyName(dto.getFrequencyName())
                    .frequencyCode(dto.getFrequencyCode())
                    .frequencyDesc(dto.getFrequencyDesc())
                    .frequencyNumber(dto.getFrequencyNumber())
                    .frequencyCycle(dto.getFrequencyCycle())
                    .cycleUnit(dto.getCycleUnit())
                    .type(dto.getType())
                    .status(dto.getStatus())
                    .level(dto.getLevel())
                    .build());
        }
        throw new DataException(DataEnums.DATA_REPEAT, "频率名称或编码重复");
    }

    @Override
    public boolean updateType(Long id, FrequencyTypeEnum type) {
        return updateById(Frequency.builder()
                .id(id)
                .type(type)
                .build());
    }

    @Override
    public boolean updateStatus(Long id, boolean status) {
        return updateById(Frequency.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public boolean removeFrequency(Long id) {
        return removeById(id);
    }

    private int getLevel() {
        return 1;
    }
}
