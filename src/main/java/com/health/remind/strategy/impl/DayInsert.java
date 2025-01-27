package com.health.remind.strategy.impl;

import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.stereotype.Component;

/**
 * @author qtx
 * @since 2025/1/27 16:08
 */
@Component
public class DayInsert extends AbstractStrategy {

    public DayInsert() {
        super();
    }

    @Override
    public void strategyTask(RemindTask task, Frequency frequency) {

    }
}
