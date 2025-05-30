package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.common.enums.RuleTypeEnum;
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
import com.health.remind.service.RuleUserService;
import com.health.remind.util.NumUtils;
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

    private final RuleUserService ruleUserService;

    public FrequencyServiceImpl(FrequencyDetailService frequencyDetailService, RuleUserService ruleUserService) {
        this.frequencyDetailService = frequencyDetailService;
        this.ruleUserService = ruleUserService;
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
                    .startTime(frequency.getStartTime())
                    .endTime(frequency.getEndTime())
                    .crossDay(frequency.getCrossDay())
                    .frequencyDetailList(frequencyDetailService.getFrequencyDetail(id))
                    .build();
        }
        return null;
    }

    @Override
    public boolean saveOrUpdateFrequency(FrequencyDTO dto) {
        if (dto.getId() == null) {
            ruleUserService.verify(RuleTypeEnum.limit_time_rule_num, 1);
        }
        String frequencyAllKey = RedisKeys.getFrequencyAllKey(CommonMethod.getAccount());
        RedisUtils.delete(frequencyAllKey);
        long count = count(Wrappers.lambdaQuery(Frequency.class)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .in(Frequency::getSource, "system", CommonMethod.getAccount())
                .and(w -> w.eq(Frequency::getName, dto.getFrequencyName())
                        .or()
                        .eq(Frequency::getFrequencyCode, dto.getFrequencyCode())));
        if (count == 0) {
            removeRedis();
            Frequency frequency = Frequency.builder()
                    .id(dto.getId())
                    .frequencyName(dto.getFrequencyName())
                    .frequencyCode(StringUtils.isBlank(dto.getFrequencyCode()) ? NumUtils.uuid() :
                            dto.getFrequencyCode())
                    .frequencyDesc(dto.getFrequencyDesc())
                    .frequencyNumber(dto.getFrequencyNumber())
                    .frequencyCycle(dto.getFrequencyCycle())
                    .cycleUnit(dto.getCycleUnit())
                    .type(dto.getType())
                    .status(dto.getStatus())
                    .level(dto.getLevel())
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .crossDay(dto.getCrossDay())
                    .source(StringUtils.isNotBlank(dto.getSource()) ? dto.getSource() : CommonMethod.getAccount()
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

    @Override
    public boolean removeAppFrequencyById(Long id) {
        Frequency byId =
                Optional.ofNullable(getById(id))
                        .orElseThrow(() -> new DataException(DataEnums.DATA_NOT_EXIST, "频率不存在"));
        if (byId.getSource()
                .equals(String.valueOf(CommonMethod.getAccount()))) {
            ruleUserService.verify(RuleTypeEnum.limit_time_rule_num, -1);
            String frequencyAllKey = RedisKeys.getFrequencyAllKey(CommonMethod.getAccount());
            RedisUtils.delete(frequencyAllKey);
            removeById(id);
            return true;
        }
        return false;
    }

    private int getLevel() {
        return 1;
    }

    private void removeRedis() {
        Set<String> keys = RedisUtils.keys(RedisKeys.getFrequencyAllKey(null));
        RedisUtils.delete(keys);
    }
}
