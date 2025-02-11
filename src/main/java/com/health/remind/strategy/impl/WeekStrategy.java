package com.health.remind.strategy.impl;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author QQQtx
 * @since 2025/1/27 16:38
 */
@Component
public class WeekStrategy extends AbstractStrategy {

    public WeekStrategy() {
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

    private void getTaskInfos(RemindTask task, FrequencyDetailVO e, List<RemindTaskInfo> remindTaskInfos) {
        remindTaskInfos.add(RemindTaskInfo.builder()
                .remindTaskId(task.getId())
                .time(LocalDateTime.of(task.getInitTime(), e.getFrequencyTime()))
                .isRemind(task.getIsRemind())
                .build());
    }

    private List<RemindTaskInfo> getTaskInfos(RemindTask task, FrequencyVO frequency) {
        List<FrequencyDetailVO> detailList = frequency.getFrequencyDetailList();
        LocalDateTime startTime = task.getStartTime();
        LocalDate initTime = task.getInitTime();
        List<RemindTaskInfo> taskInfos = new ArrayList<>();
        if (detailList != null && !detailList.isEmpty()) {
            List<FrequencyDetailVO> collect = filter(task, frequency);
            if (frequency.getStatus()) {
                Optional<Integer> first = collect.stream()
                        .map(FrequencyDetailVO::getFrequencyWeekday)
                        .findFirst();
                first.ifPresent(integer -> collect.forEach(e -> {
                    if (startTime.toLocalDate()
                            .until(initTime,
                                    ChronoUnit.DAYS) % (frequency.getFrequencyCycle() * 7L) + integer == e.getFrequencyWeekday()) {
                        getTaskInfos(task, e, taskInfos);
                    }
                }));
            } else {
                for (final FrequencyDetailVO e : collect) {
                    boolean b = e.getFrequencyWeekday() == initTime.getDayOfWeek()
                            .getValue();
                    if (b) {
                        getTaskInfos(task, e, taskInfos);
                    }
                }
            }
        }
        return taskInfos;
    }
}
