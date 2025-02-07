package com.health.remind.scheduler;

import com.health.remind.config.enums.UserInfo;
import com.health.remind.scheduler.entity.DelayTask;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 单一延时
 *
 * @author QQQtx
 * @since 2025/2/6 13:03
 */
@Slf4j
@Component
public class DelayScheduledExecutor extends ScheduledBase {

    // 调度线程池
    private final ScheduledExecutorService scheduler;

    public DelayScheduledExecutor(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @SneakyThrows
    public static void putTestTask(Long taskId, LocalDateTime executeTime, Map<UserInfo, String> commonMethod) {
        log.info("添加任务:{}", taskId);
        delayScheduledExecutorQueue.put(new DelayTask(taskId, executeTime, commonMethod));
    }

    @PostConstruct
    public void start() {
        // 启动监听线程
        scheduler.scheduleAtFixedRate(this::processTasks, 0, 1, TimeUnit.SECONDS);
    }

    private void processTasks() {
        try {
            // 获取队列中的任务
            DelayTask task = delayScheduledExecutorQueue.poll();
            if (task == null) {
                return;
            }
            LocalDateTime executeTime = task.getExecuteTime();
            long delay = Duration.between(LocalDateTime.now(), executeTime)
                    .toMillis();
            if (delay <= 0) {
                executeTask(task);
            } else {
                ScheduledFuture<?> schedule = scheduler.schedule(() -> executeTask(task), delay,
                        TimeUnit.MILLISECONDS);
                delayScheduledExecutorFutureMap.put(task.getId(), schedule);
            }
        } catch (Exception e) {
            log.error("处理任务时发生错误", e);
        }
    }

    private void executeTask(DelayTask task) {
        long start = System.currentTimeMillis();
        long between = ChronoUnit.SECONDS.between(task.getLastExecutionTime(), LocalDateTime.now());
        delayScheduledExecutorFutureMap.remove(task.getId());
        long end = System.currentTimeMillis();
        log.info("执行任务:{},延时间隔:{}秒,任务执行时间:{}毫秒", task.getId(), between, end - start);
    }
}
