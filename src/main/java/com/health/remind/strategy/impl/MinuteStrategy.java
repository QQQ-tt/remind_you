package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:38
 */
@Component
public class MinuteStrategy extends AbstractStrategy {

    public MinuteStrategy() {
        super();
    }

    @Override
    public void strategyTask(RemindTask task, FrequencyVO frequency) {

    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        return List.of();
    }
}
