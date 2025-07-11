package com.health.remind.strategy.impl;

import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:31
 */
public class DoNothing extends AbstractStrategy {

    private static final Logger log = LoggerFactory.getLogger(DoNothing.class);

    @Override
    public void strategyTask(RemindTask task, FrequencyVO frequency) {
        log.info("DoNothing");
    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        return List.of();
    }
}
