package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.FrequencyDetail;
import com.health.remind.mapper.FrequencyDetailMapper;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.FrequencyDetailPageDTO;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.service.FrequencyDetailService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 频次详情表(时间明细表) 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Service
public class FrequencyDetailServiceImpl extends ServiceImpl<FrequencyDetailMapper, FrequencyDetail> implements FrequencyDetailService {

    @Override
    public Page<FrequencyDetailVO> pageFrequencyDetail(FrequencyDetailPageDTO dto) {
        Page<FrequencyDetailVO> page = baseMapper.selectPageFrequencyDetail(dto.getPage(),
                Wrappers.lambdaQuery(FrequencyDetail.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(FrequencyDetail::getFrequencyId, dto.getFrequencyId())
                        .orderByDesc(BaseEntity::getCreateTime));
        page.getRecords()
                .forEach(this::setFrequencyTimeString);
        return page;
    }

    @Override
    public List<FrequencyDetailVO> getFrequencyDetail(Long frequencyId) {
        List<FrequencyDetailVO> list = baseMapper.selectListByFrequencyId(frequencyId);
        list.forEach(this::setFrequencyTimeString);
        return list;
    }

    @Override
    public boolean saveOrUpdateFrequencyDetail(FrequencyDetailDTO frequencyDetail) {
        long count = count(Wrappers.lambdaQuery(FrequencyDetail.class)
                .eq(FrequencyDetail::getFrequencyTime,
                        frequencyDetail.getFrequencyTime())
                .eq(FrequencyDetail::getFrequencyId, frequencyDetail.getFrequencyId())
                .eq(frequencyDetail.getFrequencyWeekday() != null, FrequencyDetail::getFrequencyWeekday,
                        frequencyDetail.getFrequencyWeekday())
                .ne(frequencyDetail.getId() != null, BaseEntity::getId,
                        frequencyDetail.getId()));
        if (count == 0) {
            return saveOrUpdate(FrequencyDetail.builder()
                    .id(frequencyDetail.getId())
                    .frequencyId(frequencyDetail.getFrequencyId())
                    .frequencyWeekday(frequencyDetail.getFrequencyWeekday())
                    .frequencyTime(frequencyDetail.getFrequencyTime())
                    .beforeRuleTime(frequencyDetail.getBeforeRuleTime())
                    .afterRuleTime(frequencyDetail.getAfterRuleTime())
                    .build());
        }
        throw new DataException(DataEnums.DATA_REPEAT, "该时间已存在");
    }

    @Override
    public boolean removeFrequencyDetailById(Long id) {
        return removeById(id);
    }

    public void setFrequencyTimeString(FrequencyDetailVO e) {
        LocalTime localTime = e.getFrequencyTime();
        if (localTime != null) {
            if (e.getFrequencyWeekday() != null) {
                e.setFrequencyTimeName(weekDay(e.getFrequencyWeekday()) + ":" + localTime);
            } else {
                e.setFrequencyTimeName(localTime.toString());
            }
        }
    }

    public String weekDay(Integer weekDay) {
        return switch (weekDay) {
            case 2 -> "周二";
            case 3 -> "周三";
            case 4 -> "周四";
            case 5 -> "周五";
            case 6 -> "周六";
            case 7 -> "周日";
            default -> "周一";
        };
    }
}
