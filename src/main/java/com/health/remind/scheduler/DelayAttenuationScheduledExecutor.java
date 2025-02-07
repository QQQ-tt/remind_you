package com.health.remind.scheduler;

import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.QueryEnum;
import com.health.remind.scheduler.enums.TimeEnum;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 衰减延时轮询
 *
 * @author QQQtx
 * @since 2025/1/14 17:16
 */
@Slf4j
@Component
public class DelayAttenuationScheduledExecutor extends ScheduledBase {

    // 调度线程池
    private final ScheduledExecutorService scheduler;

    public DelayAttenuationScheduledExecutor(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @SneakyThrows
    public static void putTestTask(Long taskId, Map<UserInfo, String> commonMethod) {
        delayAttenuationScheduledExecutorQueue.put(new DelayTask(taskId, QueryEnum.test, commonMethod));
    }

    @PostConstruct
    public void start() {
        // 启动监听线程
        scheduler.scheduleAtFixedRate(this::processTasks, 0, 1, TimeUnit.SECONDS);
    }

    private void processTasks() {
        try {
            // 获取队列中的任务
            List<DelayTask> list = new ArrayList<>();
            delayAttenuationScheduledExecutorQueue.drainTo(list);
            if (list.isEmpty()) {
                return;
            }
            list.forEach(task -> {
                LocalDateTime lastExecutionTime = task.getLastExecutionTime();
                Integer minute = TimeEnum.getSort(task.getAttemptCount()
                        .get());
                if (minute != null) {
                    LocalDateTime nextExecutionTime = lastExecutionTime.plusSeconds(minute);
                    long delay = Duration.between(LocalDateTime.now(), nextExecutionTime)
                            .toMillis();
                    if (delay <= 0 || task.getAttemptCount()
                            .get() == 0) {
                        executeTask(task);
                    } else {
                        ScheduledFuture<?> schedule = scheduler.schedule(() -> executeTask(task), delay,
                                TimeUnit.MILLISECONDS);
                        delayAttenuationScheduledExecutorFutureMap.put(task.getId(), schedule);
                    }
                } else {
                    log.error("无法获取时间间隔,循环结束，任务ID: {}", task.getId());
                }
            });
        } catch (Exception e) {
            log.error("处理任务时发生错误", e);
        }
    }

    private void executeTask(DelayTask task) {
        try {
            task.setLastExecutionTime(LocalDateTime.now());
            switch (task.getQueryEnum()) {
                case pay -> processPaymentQuery(task);
                case refund -> processRefundQuery(task);
                case test -> testQuery(task);
                default -> throw new IllegalArgumentException("无效的任务类型");
            }
            task.getAttemptCount()
                    .incrementAndGet();
        } catch (Exception e) {
            log.error("执行任务时发生错误，任务ID: {}", task.getId(), e);
        }
    }

    private void processRefundQuery(DelayTask task) {
    }

    private void processPaymentQuery(DelayTask task) {
    }

    private void testQuery(DelayTask task) {
        CommonMethod.setMap(task.getCommonMethod());
        if (task.getAttemptCount()
                .get() < 6) {
            boolean offer = delayAttenuationScheduledExecutorQueue.offer(task);
            if (offer) {
                log.info("test放入队列成功:{},次数:{},时间:{}", task.getId(), task.getAttemptCount()
                        .get(), task.getLastExecutionTime());
            } else {
                log.error("test，放入队列失败");
            }
        } else {
            delayAttenuationScheduledExecutorFutureMap.remove(task.getId());
        }
    }
}
