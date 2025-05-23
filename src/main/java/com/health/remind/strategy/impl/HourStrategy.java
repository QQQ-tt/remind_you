package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:38
 */
@Component
public class HourStrategy extends AbstractStrategy {

    @Override
    @Transactional
    @Async("customExecutor")
    public void strategyTask(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> taskInfos = getTaskInfos(task, frequency);
        save(taskInfos);
    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> list = getTaskInfos(task, frequency);
        return list.size() > 10 ? list.subList(0, 10) : list;
    }

    private List<RemindTaskInfo> getTaskInfos(RemindTask task, FrequencyVO frequency) {
        long num = 24;
        LocalTime startTime = LocalTime.of(0, 0);
        if (frequency.getStartTime() != null && frequency.getEndTime() != null) {
            Duration duration = Duration.between(frequency.getStartTime(), frequency.getEndTime());
            num = duration.toHours();
            startTime = frequency.getStartTime();
        }
        // 周期数
        int i = new BigDecimal(num).divide(BigDecimal.valueOf(frequency.getFrequencyCycle()), 2, RoundingMode.FLOOR)
                .intValue();
        List<LocalTime> localTimes = new ArrayList<>();
        List<RemindTaskInfo> list = new ArrayList<>();
        // 时间间隔
        int length = (int) (60 * frequency.getFrequencyCycle() / frequency.getFrequencyNumber());
        for (int j = 0; j <= i; j++) {
            for (int y = 0; y < frequency.getFrequencyNumber(); y++) {
                localTimes.add(startTime
                        .plusMinutes((long) (j * frequency.getFrequencyCycle() * 60L + (long) y * length)));
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
        int i = calculateTime(task);
        times.forEach(e -> {
            LocalDateTime time = LocalDateTime.of(task.getInitTime(), e);
            list.add(RemindTaskInfo.builder()
                    .remindTaskId(task.getId())
                    .remindTaskName(task.getName())
                    .estimatedTime(time.minusMinutes(i))
                    .time(time)
                    .isRemind(task.getIsRemind())
                    .remindType(task.getRemindType())
                    .status(task.getStatus())
                    .email(task.getEmail())
                    .build());
        });
    }
}
