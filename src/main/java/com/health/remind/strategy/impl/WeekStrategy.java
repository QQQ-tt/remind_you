package com.health.remind.strategy.impl;

import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.config.CommonMethod;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.strategy.AbstractStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        List<FrequencyDetailVO> detailList = frequency.getFrequencyDetailList();
        LocalDateTime startTime = task.getStartTime();
        LocalDate initTime = task.getInitTime();
        List<RemindTaskInfo> taskInfos = new ArrayList<>();
        if (detailList != null && !detailList.isEmpty()) {
            List<FrequencyDetailVO> collect = filter(task, frequency);
            if (frequency.getType() == FrequencyTypeEnum.LOGIC_WEEK) {
                Optional<Integer> first = collect.stream()
                        .map(FrequencyDetailVO::getFrequencyWeekday)
                        .findFirst();
                first.ifPresent(integer -> collect.forEach(e -> {
                    if (startTime.toLocalDate()
                            .until(initTime,
                                    ChronoUnit.DAYS) % (frequency.getFrequencyCycle() * 7L) + integer == e.getFrequencyWeekday()) {
                        addExecutionTask(task, e, taskInfos);
                    }
                }));
            }
            if (frequency.getType() == FrequencyTypeEnum.NATURAL_WEEK) {
                for (final FrequencyDetailVO e : collect) {
                    boolean b = e.getFrequencyWeekday() == initTime.getDayOfWeek()
                            .getValue();
                    if (b) {
                        addExecutionTask(task, e, taskInfos);
                    }
                }
            }
        }
        return taskInfos;
    }

    private void addExecutionTask(RemindTask task, FrequencyDetailVO e, List<RemindTaskInfo> remindTaskInfos) {
        int i = calculateTime(task);
        LocalDateTime time = LocalDateTime.of(task.getInitTime(), e.getFrequencyTime());
        remindTaskInfos.add(RemindTaskInfo.builder()
                .remindTaskId(task.getId())
                .remindTaskName(task.getName())
                .estimatedTime(time.minusMinutes(i))
                .time(time)
                .isRemind(task.getIsRemind())
                .remindType(task.getRemindType())
                .status(task.getStatus())
                .email(task.getEmail())
                .account(CommonMethod.getAccount())
                .build());
    }
}
