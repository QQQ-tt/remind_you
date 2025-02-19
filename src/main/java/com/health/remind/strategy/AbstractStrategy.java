package com.health.remind.strategy;

import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.util.SpringUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author QQQtx
 * @since 2025/1/27 16:27
 */
public abstract class AbstractStrategy implements FrequencyStrategy {

    private final static ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 0);

    protected final RemindTaskInfoService remindTaskInfoService = SpringUtils.getBean(RemindTaskInfoService.class);

    public AbstractStrategy() {
        register();
    }

    /**
     * 类注册方法
     */
    public void register() {
        StrategyContext.registerStrategy(getClass().getSimpleName()
                .toLowerCase(), this);
    }

    public static int getCount() {
        return count.get();
    }

    public static void addCount(int size) {
        count.set(count.get() + size);
    }

    public List<FrequencyDetailVO> filter(RemindTask task, FrequencyVO frequency) {
        return frequency.getFrequencyDetailList()
                .stream()
                .filter(f -> {
                    if (f.getBeforeRuleTime() != null && f.getAfterRuleTime() != null) {
                        LocalTime localTime = task.getStartTime()
                                .toLocalTime();
                        if (localTime.isAfter(f.getBeforeRuleTime()) && localTime
                                .isBefore(f.getAfterRuleTime())) {
                            return true;
                        } else return localTime.equals(f.getBeforeRuleTime()) || localTime.equals(f.getAfterRuleTime());
                    } else {
                        return true;
                    }
                })
                .toList();
    }

    /**
     * 计算要提前提醒的时间
     *
     * @param task 任务
     * @return 小时数
     */
    public static int calculateTime(RemindTask task) {
        int time = 0;
        if (task.getAdvanceNum() != null && task.getCycleUnit() != null) {
            return switch (task.getCycleUnit()) {
                case DAY -> task.getAdvanceNum() * 24;
                case WEEK -> task.getAdvanceNum() * 24 * 7;
                case MONTH -> task.getAdvanceNum() * 24 * 30;
                case HOUR -> task.getAdvanceNum();
            };
        }
        return time;
    }

    protected void save(List<RemindTaskInfo> taskInfos, ThreadPoolExecutor threadPoolExecutor) {
        if (!taskInfos.isEmpty()) {
            AbstractStrategy.addCount(taskInfos.size());
            Map<UserInfo, String> map = CommonMethod.getMap();
            CompletableFuture.runAsync(() -> {
                CommonMethod.setMap(map);
                remindTaskInfoService.saveBatch(taskInfos);
            }, threadPoolExecutor);
        }
    }
}
