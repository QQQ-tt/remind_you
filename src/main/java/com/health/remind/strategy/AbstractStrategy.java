package com.health.remind.strategy;

import com.health.remind.entity.RemindTask;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.util.SpringUtils;

import java.time.LocalTime;
import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:27
 */
public abstract class AbstractStrategy implements FrequencyStrategy {

    protected final RemindTaskInfoService remindTaskInfoService = SpringUtils.getBean(RemindTaskInfoService.class);

    public AbstractStrategy() {
        register();
    }
    /**
     * 类注册方法
     */
    public void register() {
        StrategyContext.registerStrategy(getClass().getSimpleName()
                .toLowerCase(), this);
    }

    public List<FrequencyDetailVO> filter(RemindTask task, FrequencyVO frequency) {
        return frequency.getFrequencyDetailList()
                .stream()
                .filter(f -> {
                    if (f.getBeforeRuleTime() != null && f.getAfterRuleTime() != null) {
                        LocalTime localTime = task.getStartTime()
                                .toLocalTime();
                        if (localTime.isAfter(f.getBeforeRuleTime()) && localTime
                                .isBefore(f.getAfterRuleTime())) {
                            return true;
                        } else return localTime.equals(f.getBeforeRuleTime()) || localTime.equals(f.getAfterRuleTime());
                    } else {
                        return true;
                    }
                })
                .toList();
    }
}
