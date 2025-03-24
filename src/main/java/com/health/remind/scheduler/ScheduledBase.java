package com.health.remind.scheduler;

import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.ScheduledEnum;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * 仅适用小规模使用
 *
 * @author QQQtx
 * @since 2025/2/6 13:24
 */
public class ScheduledBase {

    // 存储任务队列
    protected final static BlockingQueue<DelayTask> delayScheduledExecutorQueue = new LinkedBlockingQueue<>();

    // 单一延时任务:存储任务ID和对应的ScheduledFuture
    protected final static ConcurrentHashMap<Long, ScheduledFuture<?>> delayScheduledExecutorFutureMap =
            new ConcurrentHashMap<>();

    // 存储任务队列
    protected final static BlockingQueue<DelayTask> delayAttenuationScheduledExecutorQueue = new LinkedBlockingQueue<>();

    // 衰减延时任务:存储任务ID和对应的ScheduledFuture
    protected final static ConcurrentHashMap<Long, ScheduledFuture<?>> delayAttenuationScheduledExecutorFutureMap =
            new ConcurrentHashMap<>();

    public static void cancelTask(Long taskId, ScheduledEnum scheduledEnum, boolean removeTaskAll) {
        switch (scheduledEnum) {
            case DELAY_SCHEDULED:
                removeTask(taskId, delayScheduledExecutorFutureMap, removeTaskAll);
                break;
            case DELAY_ATTENUATION_SCHEDULED:
                removeTask(taskId, delayAttenuationScheduledExecutorFutureMap, removeTaskAll);
                break;
            default:
                break;
        }
    }

    public static boolean containsTask(Long taskId, ScheduledEnum scheduledEnum) {
        return switch (scheduledEnum) {
            case DELAY_SCHEDULED -> delayScheduledExecutorFutureMap.containsKey(taskId);
            case DELAY_ATTENUATION_SCHEDULED -> delayAttenuationScheduledExecutorFutureMap.containsKey(taskId);
            default -> false;
        };
    }

    public static int getTaskSize(ScheduledEnum scheduledEnum) {
        return switch (scheduledEnum) {
            case DELAY_SCHEDULED -> delayScheduledExecutorQueue.size();
            case DELAY_ATTENUATION_SCHEDULED -> delayAttenuationScheduledExecutorQueue.size();
            default -> 0;
        };
    }

    public static int getFutureSize(ScheduledEnum scheduledEnum) {
        return switch (scheduledEnum) {
            case DELAY_SCHEDULED -> delayScheduledExecutorFutureMap.size();
            case DELAY_ATTENUATION_SCHEDULED -> delayAttenuationScheduledExecutorFutureMap.size();
            default -> 0;
        };
    }

    private static void removeTask(Long taskId, ConcurrentHashMap<Long, ScheduledFuture<?>> map, boolean removeTaskAll) {
        if (taskId != null) {
            if (map.containsKey(taskId)) {
                ScheduledFuture<?> future = map.get(taskId);
                if (future.cancel(true)) {
                    map.remove(taskId);
                }
            }
        }
        if (taskId == null && removeTaskAll) {
            map.forEach((k, v) -> {
                if (v.cancel(true)) {
                    map.remove(k);
                }
            });
        }
    }
}
