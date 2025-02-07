package com.health.remind.scheduler;

import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.ScheduledEnum;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * 仅适用小规模任我
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

    public static void cancelTask(Long taskId, ScheduledEnum scheduledEnum) {
        switch (scheduledEnum) {
            case DELAY_SCHEDULED:
                ScheduledFuture<?> future = delayScheduledExecutorFutureMap.get(taskId);
                if (future != null) {
                    if (future.cancel(true)) {
                        delayScheduledExecutorFutureMap.remove(taskId);
                    }
                }
                break;
            case DELAY_ATTENUATION_SCHEDULED:
                ScheduledFuture<?> future1 = delayAttenuationScheduledExecutorFutureMap.get(taskId);
                if (future1 != null) {
                    if (future1.cancel(true)) {
                        delayAttenuationScheduledExecutorFutureMap.remove(taskId);
                    }
                }
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
}
