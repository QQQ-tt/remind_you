package com.health.remind.strategy;

import com.health.remind.common.enums.FrequencySqlTypeEnum;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.dto.FrequencyDTO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.FrequencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:42
 */
@Slf4j
@Component
public class FrequencyUtils {

    private final FrequencyService frequencyService;

    public FrequencyUtils(FrequencyService frequencyService) {
        this.frequencyService = frequencyService;
    }

    public List<RemindTaskInfo> splitTask(RemindTask task, FrequencySqlTypeEnum typeEnum) {
        if (task == null) {
            return List.of();
        }
        Long frequencyId = task.getFrequencyId();
        FrequencyVO frequency = frequencyService.getFrequency(frequencyId);
        LocalDateTime startTime = task.getStartTime() == null ? LocalDateTime.now() : task.getStartTime();
        LocalDateTime endTime = task.getEndTime() == null ? startTime.plusHours(24) : task.getEndTime();
        if (typeEnum.equals(FrequencySqlTypeEnum.INSERT)) {
            for (; startTime.isBefore(endTime); startTime = startTime.plusDays(1)) {
                task.setInitTime(startTime.toLocalDate());
                RemindTask remindTask = copyTask(task);
                StrategyContext.getStrategy(frequency.getCycleUnit()
                                .getValue() + "strategy")
                        .strategyTask(remindTask, frequency);
            }
        }
        if (typeEnum.equals(FrequencySqlTypeEnum.SELECT)) {
            return getRemindTaskInfos(task, startTime, frequency);
        }
        return List.of();
    }

    public List<RemindTaskInfo> testSplitTask(FrequencyDTO frequency) {
        if (frequency == null) {
            return List.of();
        }
        FrequencyVO vo = FrequencyVO.builder()
                .name(frequency.getFrequencyName())
                .frequencyCode(frequency.getFrequencyCode())
                .frequencyDesc(frequency.getFrequencyDesc())
                .frequencyNumber(frequency.getFrequencyNumber())
                .frequencyCycle(frequency.getFrequencyCycle())
                .startTime(frequency.getStartTime())
                .endTime(frequency.getEndTime())
                .crossDay(frequency.getCrossDay())
                .cycleUnit(frequency.getCycleUnit())
                .type(frequency.getType())
                .build();
        RemindTask task = RemindTask.builder()
                .id(1L)
                .name("测试任务")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now()
                        .plusDays(10))
                .remark("测试任务")
                .status(true)
                .isRemind(true)
                .build();
        return getRemindTaskInfos(task, LocalDateTime.now(), vo);
    }

    private static List<RemindTaskInfo> getRemindTaskInfos(RemindTask task, LocalDateTime startTime, FrequencyVO frequency) {
        List<RemindTaskInfo> list = new ArrayList<>();
        while (list.size() < 10) {
            log.info("开始时间:{}", startTime);
            task.setInitTime(startTime.toLocalDate());
            list.addAll(StrategyContext.getStrategy(frequency.getCycleUnit()
                            .getValue() + "strategy")
                    .strategyTaskNumTen(task, frequency));
            startTime = startTime.plusDays(1);
        }
        return list.size() > 10 ? list.subList(0, 10) : list;
    }

    private RemindTask copyTask(RemindTask source) {
        return RemindTask.builder()
                .id(source.getId())
                .name(source.getName())
                .startTime(source.getStartTime())
                .endTime(source.getEndTime())
                .remark(source.getRemark())
                .status(source.getStatus())
                .isRemind(source.getIsRemind())
                .remindType(source.getRemindType())
                .advanceNum(source.getAdvanceNum())
                .cycleUnit(source.getCycleUnit())
                .frequencyId(source.getFrequencyId())
                .email(source.getEmail())
                .initTime(source.getInitTime())
                .build();
    }
}
