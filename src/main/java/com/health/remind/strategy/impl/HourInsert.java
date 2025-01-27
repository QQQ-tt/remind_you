package com.health.remind.strategy.impl;

import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.stereotype.Component;

/**
 * @author qtx
 * @since 2025/1/27 16:38
 */
@Component
public class HourInsert extends AbstractStrategy {

    public HourInsert() {
        super();
    }

    @Override
    public void strategyTask(RemindTask task, Frequency frequency) {

    }
}
