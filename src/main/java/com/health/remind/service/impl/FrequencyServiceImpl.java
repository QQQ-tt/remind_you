package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.Frequency;
import com.health.remind.mapper.FrequencyMapper;
import com.health.remind.pojo.dto.FrequencyDTO;
import com.health.remind.pojo.dto.FrequencyPageDTO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.FrequencyDetailService;
import com.health.remind.service.FrequencyService;
import com.health.remind.util.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final FrequencyDetailService frequencyDetailService;

    public FrequencyServiceImpl(FrequencyDetailService frequencyDetailService) {
        this.frequencyDetailService = frequencyDetailService;
    }

    @Override
    public Page<FrequencyVO> pageFrequency(FrequencyPageDTO dto) {
        return baseMapper.selectPageFrequency(dto.getPage(),
                Wrappers.lambdaQuery(Frequency.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(dto.getStatus() != null, Frequency::getStatus, dto.getStatus())
                        .eq(dto.getCycleUnit() != null, Frequency::getCycleUnit, dto.getCycleUnit())
                        .like(StringUtils.isNotBlank(dto.getName()),
                                Frequency::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public List<FrequencyVO> listFrequency() {
        String frequencyAllKey = RedisKeys.getFrequencyAllKey(CommonMethod.getAccount());
        boolean flag = RedisUtils.hasKey(frequencyAllKey);
        if (flag) {
            return RedisUtils.getObject(frequencyAllKey, new TypeReference<>() {
            });
        }
        List<FrequencyVO> sys = baseMapper.selectListFrequency(Wrappers.lambdaQuery(Frequency.class)
                .eq(BaseEntity::getDeleteFlag, false)
                .le(Frequency::getLevel, getLevel())
                .eq(Frequency::getStatus, true)
                .in(Frequency::getSource, "system",
                        CommonMethod.getAccount()));
        Optional.ofNullable(sys)
                .ifPresent(s -> RedisUtils.setObject(frequencyAllKey, s));
        return sys;
    }

    @Override
    public FrequencyVO getFrequency(Long id) {
        Frequency frequency = getById(id);
        if (frequency != null) {
            return FrequencyVO.builder()
                    .id(frequency.getId())
                    .name(frequency.getName())
                    .frequencyCode(frequency.getFrequencyCode())
                    .frequencyDesc(frequency.getFrequencyDesc())
                    .frequencyNumber(frequency.getFrequencyNumber())
                    .frequencyCycle(frequency.getFrequencyCycle())
                    .cycleUnit(frequency.getCycleUnit())
                    .type(frequency.getType())
                    .status(frequency.getStatus())
                    .frequencyDetailList(frequencyDetailService.getFrequencyDetail(id))
                    .build();
        }
        return null;
    }

    @Override
    public boolean saveOrUpdateFrequency(FrequencyDTO dto) {
        boolean b = StringUtils.isNotBlank(dto.getFrequencyCode()) && StringUtils.isNotBlank(dto.getFrequencyName());
        if (!b) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL);
        }
        long count = count(Wrappers.lambdaQuery(Frequency.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .and(w -> w.eq(Frequency::getName, dto.getFrequencyName())
                        .or()
                        .eq(Frequency::getFrequencyCode, dto.getFrequencyCode())));
        if (count == 0) {
            removeRedis();
            Frequency frequency = Frequency.builder()
                    .frequencyName(dto.getFrequencyName())
                    .frequencyCode(dto.getFrequencyCode())
                    .frequencyDesc(dto.getFrequencyDesc())
                    .frequencyNumber(dto.getFrequencyNumber())
                    .frequencyCycle(dto.getFrequencyCycle())
                    .cycleUnit(dto.getCycleUnit())
                    .type(dto.getType())
                    .status(dto.getStatus())
                    .level(dto.getLevel())
                    .source(StringUtils.isBlank(dto.getSource()) ? dto.getSource() : CommonMethod.getAccount()
                            .toString())
                    .build();
            boolean savedOrUpdate = saveOrUpdate(frequency);
            dto.setId(frequency.getId());
            return savedOrUpdate;
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
        removeRedis();
        return updateById(Frequency.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public boolean removeFrequencyById(Long id) {
        removeRedis();
        return removeById(id);
    }

    private int getLevel() {
        return 1;
    }

    private void removeRedis() {
        Set<String> keys = RedisUtils.keys(RedisKeys.getFrequencyAllKey(null));
        RedisUtils.delete(keys);
    }
}
