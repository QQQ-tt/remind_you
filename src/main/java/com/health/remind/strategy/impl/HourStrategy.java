package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import lombok.val;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author QQQtx
 * @since 2025/1/27 16:38
 */
@Component
public class HourStrategy extends AbstractStrategy {

    public HourStrategy() {
        super();
    }

    @Override
    public void strategyTask(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> taskInfos = getTaskInfos(task, frequency);
        if (!taskInfos.isEmpty()) {
            AbstractStrategy.addCount(taskInfos.size());
            remindTaskInfoService.saveBatch(taskInfos);
        }
    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> list = getTaskInfos(task, frequency);
        return list.size() > 10 ? list.subList(0, 10) : list;
    }

    private List<RemindTaskInfo> getTaskInfos(RemindTask task, FrequencyVO frequency) {
        // 周期数
        int i = new BigDecimal(24).divide(new BigDecimal(frequency.getFrequencyCycle()), 2, RoundingMode.FLOOR)
                .intValue();
        List<LocalTime> localTimes = new ArrayList<>();
        List<RemindTaskInfo> list = new ArrayList<>();
        // 时间间隔
        int length = 60 * frequency.getFrequencyCycle() / frequency.getFrequencyNumber();
        for (int j = 0; j < i; j++) {
            for (int y = 0; y < frequency.getFrequencyNumber(); y++) {
                localTimes.add(LocalTime.of(j * frequency.getFrequencyCycle(), 0)
                        .plusMinutes((long) y * length));
            }
        }
        if (task.getStartTime()
                .toLocalDate()
                .equals(task.getInitTime())) {
            localTimes = localTimes.stream()
                    .filter(f -> f.isAfter(LocalTime.now()))
                    .toList();
        }
        addExecutionTask(task, localTimes, list);
        return list;
    }

    private static void addExecutionTask(RemindTask task, List<LocalTime> times, List<RemindTaskInfo> list) {
        times.forEach(e -> list.add(RemindTaskInfo.builder()
                .remindTaskId(task.getId())
                .time(LocalDateTime.of(task.getInitTime(), e))
                .isRemind(task.getIsRemind())
                .build()));
    }
}
