package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author QQQtx
 * @since 2025/1/27 16:08
 */
@Component
public class DayStrategy extends AbstractStrategy {

    private final ThreadPoolExecutor threadPoolExecutor;

    public DayStrategy(ThreadPoolExecutor threadPoolExecutor) {
        super();
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void strategyTask(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> taskInfos = getTaskInfos(task, frequency);
        save(taskInfos, threadPoolExecutor);
    }

    @Override
    public List<RemindTaskInfo> strategyTaskNumTen(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> list = getTaskInfos(task, frequency);
        return list.size() > 10 ? list.subList(0, 10) : list;
    }

    private List<RemindTaskInfo> getTaskInfos(RemindTask task, FrequencyVO frequency) {
        List<RemindTaskInfo> list = new ArrayList<>();
        if (task.getInitTime()
                .compareTo(task.getStartTime()
                        .toLocalDate()) % frequency.getFrequencyCycle() == 0) {
            if (task.getStartTime()
                    .toLocalDate()
                    .equals(LocalDate.now())) {
                frequency.setFrequencyDetailList(filter(task, frequency));
            }
            addExecutionTask(task, frequency.getFrequencyDetailList(), list);
        }
        return list;
    }

    private static void addExecutionTask(RemindTask task, List<FrequencyDetailVO> collect, List<RemindTaskInfo> taskInfos) {
        int i = calculateTime(task);
        collect.forEach(e -> {
            LocalDateTime time = LocalDateTime.of(task.getInitTime(), e.getFrequencyTime());
            taskInfos.add(RemindTaskInfo.builder()
                    .remindTaskId(task.getId())
                    .estimatedTime(time.minusHours(i))
                    .time(time)
                    .isRemind(task.getIsRemind())
                    .remindType(task.getRemindType())
                    .build());
        });
    }
}
