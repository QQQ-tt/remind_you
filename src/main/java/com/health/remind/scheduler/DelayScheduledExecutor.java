package com.health.remind.scheduler;

import com.health.remind.config.enums.UserInfo;
import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.ExecutionEnum;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
    public static void putTestTask(Long taskId, LocalDateTime executeTime,
                                   Map<UserInfo, String> commonMethod) {
        delayScheduledExecutorQueue.put(new DelayTask(taskId, executeTime, ExecutionEnum.test, commonMethod));
    }

    @SneakyThrows
    public static void putRemindTask(Long taskId, LocalDateTime executeTime, ExecutionEnum executionEnum, Map<UserInfo
            , String> commonMethod) {
        delayScheduledExecutorQueue.put(new DelayTask(taskId, executeTime, executionEnum, commonMethod));
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
            delayScheduledExecutorQueue.drainTo(list);
            if (list.isEmpty()) {
                return;
            }
            list.forEach(task -> {
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
            });
        } catch (Exception e) {
            log.error("处理任务时发生错误", e);
        }
    }

    private void executeTask(DelayTask task) {
        switch (task.getExecutionEnum()) {
            case test -> textTask(task);
            case remind -> remindTask(task);
            default -> throw new IllegalArgumentException("无效的任务类型");
        }
    }

    private void remindTask(DelayTask task) {
    }

    private void textTask(DelayTask task) {
        long start = System.currentTimeMillis();
        long between = ChronoUnit.SECONDS.between(task.getLastExecutionTime(), LocalDateTime.now());
        delayScheduledExecutorFutureMap.remove(task.getId());
        long end = System.currentTimeMillis();
        log.info("执行任务:{},延时间隔:{}秒,任务执行时间:{}毫秒", task.getId(), between, end - start);
    }
}
