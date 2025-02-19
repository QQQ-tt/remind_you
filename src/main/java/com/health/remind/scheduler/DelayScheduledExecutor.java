package com.health.remind.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
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

    private final RemindTaskService remindTaskService;

    private final RemindTaskInfoService remindTaskInfoService;

    public DelayScheduledExecutor(ScheduledExecutorService scheduler, RemindTaskService remindTaskService, RemindTaskInfoService remindTaskInfoService) {
        this.scheduler = scheduler;
        this.remindTaskService = remindTaskService;
        this.remindTaskInfoService = remindTaskInfoService;
    }

    @SneakyThrows
    public static void putTestTask(Long taskId, LocalDateTime executeTime,
                                   Map<UserInfo, String> commonMethod) {
        delayScheduledExecutorQueue.put(new DelayTask(taskId, executeTime, RemindTypeEnum.test, commonMethod, ""));
    }

    @SneakyThrows
    public static void putRemindTask(Long taskId, Long remindId, LocalDateTime executeTime,
                                     RemindTypeEnum remindTypeEnum,
                                     Map<UserInfo, String> commonMethod) {
        delayScheduledExecutorQueue.put(
                new DelayTask(taskId, executeTime, remindTypeEnum, commonMethod, remindId.toString()));
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
        switch (task.getRemindTypeEnum()) {
            case test -> textTask(task);
            case remind_text -> remindTextTask(task);
            case remind_wx -> remindWxTask(task);
            default -> throw new IllegalArgumentException("无效的任务类型");
        }
        delayScheduledExecutorFutureMap.remove(task.getId());
    }

    private void remindTextTask(DelayTask task) {
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .setSql("pushNum = " + "pushNum + 1"));
        RemindTaskInfo info = new RemindTaskInfo();
        info.setId(task.getId());
        info.setIsSend(true);
        // 发送消息
        remindTaskInfoService.updateById(info);
    }

    private void remindWxTask(DelayTask task) {
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .setSql("pushNum = " + "pushNum + 1"));
        RemindTaskInfo info = new RemindTaskInfo();
        info.setId(task.getId());
        info.setIsSend(true);
        // 发送消息
        remindTaskInfoService.updateById(info);
    }

    private void textTask(DelayTask task) {
        long start = System.currentTimeMillis();
        long between = ChronoUnit.SECONDS.between(task.getLastExecutionTime(), LocalDateTime.now());
        long end = System.currentTimeMillis();
        log.info("执行任务:{},延时间隔:{}秒,任务执行时间:{}毫秒", task.getId(), between, end - start);
    }
}
