package com.health.remind.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencySqlTypeEnum;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.scheduler.DelayScheduledExecutor;
import com.health.remind.scheduler.enums.ExecutionEnum;
import com.health.remind.service.FrequencyService;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author QQQtx
 * @since 2025/1/27 16:42
 */
@Component
public class FrequencyUtils {

    private final FrequencyService frequencyService;

    private final RemindTaskService remindTaskService;

    private final RemindTaskInfoService remindTaskInfoService;

    private final ThreadPoolExecutor threadPoolExecutor;

    public FrequencyUtils(FrequencyService frequencyService, RemindTaskService remindTaskService, RemindTaskInfoService remindTaskInfoService, ThreadPoolExecutor threadPoolExecutor) {
        this.frequencyService = frequencyService;
        this.remindTaskService = remindTaskService;
        this.remindTaskInfoService = remindTaskInfoService;
        this.threadPoolExecutor = threadPoolExecutor;
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
                StrategyContext.getStrategy(frequency.getCycleUnit()
                                .getValue() + "strategy")
                        .strategyTask(task, frequency);
            }
            int count = AbstractStrategy.getCount();
            task.setNum(count);
            remindTaskService.updateById(task);
            Map<UserInfo, String> map = CommonMethod.getMap();
            if (task.getIsRemind()) {
                CompletableFuture.runAsync(() -> {
                    List<RemindTaskInfo> list = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                            .eq(RemindTaskInfo::getRemindTaskId, task.getId()));
                    Integer advanceNum = task.getAdvanceNum();
                    FrequencyEnum cycleUnit = task.getCycleUnit();
                    boolean b = advanceNum != null && cycleUnit != null;
                    list.forEach(e -> {
                        if (b) {
                            switch (cycleUnit) {
                                case DAY:
                                    e.setTime(e.getTime()
                                            .minusDays(advanceNum));
                                    break;
                                case WEEK:
                                    e.setTime(e.getTime()
                                            .minusWeeks(advanceNum));
                                    break;
                                case MONTH:
                                    e.setTime(e.getTime()
                                            .minusMonths(advanceNum));
                                    break;
                                case HOUR:
                                    e.setTime(e.getTime()
                                            .minusHours(advanceNum));
                            }
                        }
                        DelayScheduledExecutor.putRemindTask(e.getId(), e.getTime(), ExecutionEnum.remind, map);
                    });
                }, threadPoolExecutor);
            }
        }
        if (typeEnum.equals(FrequencySqlTypeEnum.SELECT)) {
            List<RemindTaskInfo> list = new ArrayList<>();
            while (list.size() < 10 && startTime.isBefore(endTime)) {
                task.setInitTime(startTime.toLocalDate());
                list.addAll(StrategyContext.getStrategy(frequency.getCycleUnit()
                                .getValue() + "strategy")
                        .strategyTaskNumTen(task, frequency));
                startTime = startTime.plusDays(1);
            }
            return list.size() > 10 ? list.subList(0, 10) : list;
        }
        return List.of();
    }
}
