package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author qtx
 * @since 2025/5/21 12:35
 */
@Component
public class HourManualStrategy extends AbstractStrategy {

    @Override
    @Transactional
    @Async("customExecutor")
    public void strategyTask(RemindTask task, FrequencyVO frequency) {
        RemindTaskInfo taskInfo = getTaskInfo(task, frequency, LocalTime.now());
        save(Collections.singletonList(taskInfo));
    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        ArrayList<RemindTaskInfo> list = new ArrayList<>();
        LocalTime now = LocalTime.now();
        while (list.size() < 10) {
            RemindTaskInfo taskInfo = getTaskInfo(task, frequency, now);
            now = taskInfo.getActualTime()
                    .toLocalTime();
            list.add(taskInfo);
        }
        return list;
    }

    private RemindTaskInfo getTaskInfo(RemindTask task, FrequencyVO frequency, LocalTime now) {
        int i = calculateTime(task);
        LocalDateTime executionTime = getExecutionTime(task, frequency, now);
        return RemindTaskInfo.builder()
                .remindTaskId(task.getId())
                .remindTaskName(task.getName())
                .estimatedTime(executionTime.minusHours(i))
                .time(executionTime)
                .isRemind(task.getIsRemind())
                .remindType(task.getRemindType())
                .status(task.getStatus())
                .email(task.getEmail())
                .build();
    }

    private static LocalDateTime getExecutionTime(RemindTask task, FrequencyVO frequency, LocalTime now) {
        LocalDate initTime = task.getInitTime();
        LocalDateTime localDateTime = LocalDateTime.of(initTime, now);
        LocalDateTime executionTime = localDateTime.plusHours(frequency.getFrequencyCycle());
        if (frequency.getCrossDay()) {
            if (executionTime.toLocalTime()
                    .isAfter(frequency.getEndTime())) {
                executionTime = LocalDateTime.of(initTime.plusDays(1), frequency.getStartTime());
            }
        } else {
            if (executionTime.toLocalTime()
                    .isAfter(frequency.getEndTime())) {
                executionTime = LocalDateTime.of(initTime, frequency.getEndTime());
            }
        }
        return executionTime;
    }
}
