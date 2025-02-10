package com.health.remind.strategy;

import com.health.remind.common.enums.FrequencySqlTypeEnum;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.FrequencyService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QQQtx
 * @since 2025/1/27 16:42
 */
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
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        if (typeEnum.equals(FrequencySqlTypeEnum.INSERT)) {
            for (; startTime.isBefore(endTime); startTime = startTime.plusDays(1)) {
                task.setInitTime(startTime.toLocalDate());
                StrategyContext.getStrategy(frequency.getCycleUnit().getValue() + "strategy")
                        .strategyTask(task, frequency);
            }
        }
        if (typeEnum.equals(FrequencySqlTypeEnum.SELECT)) {
            List<RemindTaskInfo> list = new ArrayList<>();
            while (list.size() < 10 && startTime.isBefore(endTime)) {
                task.setInitTime(startTime.toLocalDate());
                list.addAll(StrategyContext.getStrategy(frequency.getCycleUnit().getValue() + "strategy")
                        .strategyTaskNumTen(task, frequency));
                startTime = startTime.plusDays(1);
            }
            return list.size() > 10 ? list.subList(0, 10) : list;
        }
        return List.of();
    }
}
