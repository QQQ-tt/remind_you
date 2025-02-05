package com.health.remind.strategy;

import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;

/**
 * @author QQQtx
 * @since 2025/1/27 15:55
 */
public interface FrequencyStrategy {

    /**
     * 策略内容
     *
     * @param task      任务
     * @param frequency 频次
     */
    void strategyTask(RemindTask task, Frequency frequency);
}
