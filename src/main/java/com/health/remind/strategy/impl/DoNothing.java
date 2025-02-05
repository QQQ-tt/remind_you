package com.health.remind.strategy.impl;

import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;
import com.health.remind.strategy.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author QQQtx
 * @since 2025/1/27 16:31
 */
public class DoNothing extends AbstractStrategy {

    private static final Logger log = LoggerFactory.getLogger(DoNothing.class);

    @Override
    public void strategyTask(RemindTask task, Frequency frequency) {
        log.info("DoNothing");
    }
}
