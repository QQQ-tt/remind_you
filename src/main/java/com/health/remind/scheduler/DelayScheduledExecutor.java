package com.health.remind.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.mail.MailService;
import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import com.health.remind.scheduler.enums.ScheduledEnum;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private final MailService mailService;

    public DelayScheduledExecutor(ScheduledExecutorService scheduler, RemindTaskService remindTaskService, RemindTaskInfoService remindTaskInfoService, MailService mailService) {
        this.scheduler = scheduler;
        this.remindTaskService = remindTaskService;
        this.remindTaskInfoService = remindTaskInfoService;
        this.mailService = mailService;
    }

    @SneakyThrows
    public static void putTestTask(Long taskId, LocalDateTime executeTime,
                                   Map<UserInfo, String> commonMethod) {
        if (!containsTask(taskId, ScheduledEnum.DELAY_SCHEDULED)) {
            delayScheduledExecutorQueue.put(
                    new DelayTask(taskId, executeTime, RemindTypeEnum.test, commonMethod, "", null));
        }
    }

    @SneakyThrows
    public static void putRemindTask(Long taskId, Long remindId, LocalDateTime executeTime,
                                     RemindTypeEnum remindTypeEnum,
                                     Map<UserInfo, String> commonMethod, Map<String, String> otherMap) {
        log.info("放入任务:{},任务类型:{},执行时间:{},是否存在:{},otherMap:{}", taskId, remindTypeEnum, executeTime,
                containsTask(taskId, ScheduledEnum.DELAY_SCHEDULED), otherMap);
        if (!containsTask(taskId, ScheduledEnum.DELAY_SCHEDULED)) {
            delayScheduledExecutorQueue.put(
                    new DelayTask(taskId, executeTime, remindTypeEnum, commonMethod, remindId.toString(), otherMap));
        }
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

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(lockParameter = "#task.id", retryNum = 0)
    public void executeTask(DelayTask task) {
        log.info("执行任务:{},任务类型:{}", task.getId(), task.getRemindTypeEnum());
        CommonMethod.setMap(task.getCommonMethod());
        switch (task.getRemindTypeEnum()) {
            case test -> textTask(task);
            case remind_email -> remindEmailTask(task);
            case remind_text -> remindTextTask(task);
            case remind_wx -> remindWxTask(task);
            default -> throw new IllegalArgumentException("无效的任务类型");
        }
        delayScheduledExecutorFutureMap.remove(task.getId());
    }

    private void remindTextTask(DelayTask task) {
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .setSql("push_num = " + "push_num + 1"));
        // 发送消息
        updateStatus(task);
    }

    private void remindEmailTask(DelayTask task) {
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .setSql("push_num = " + "push_num + 1"));
        Optional.ofNullable(task.getOtherMap())
                .ifPresent(map -> {
                    if (map.containsKey(RemindTypeEnum.remind_email.toString()) && map.containsKey("NAME")) {
                        mailService.send(map.get(RemindTypeEnum.remind_email.toString()), "提醒", getRemindMsg(8, map.get("NAME")));
                    }
                });
        // 发送消息
        updateStatus(task);
    }

    private void remindWxTask(DelayTask task) {
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .setSql("push_num = " + "push_num + 1"));
        // 发送消息
        updateStatus(task);
    }

    private void textTask(DelayTask task) {
        long start = System.currentTimeMillis();
        long between = ChronoUnit.SECONDS.between(task.getLastExecutionTime(), LocalDateTime.now());
        long end = System.currentTimeMillis();
        log.info("执行任务:{},延时间隔:{}秒,任务执行时间:{}毫秒", task.getId(), between, end - start);
        updateStatus(task);
    }

    private void updateStatus(DelayTask task) {
        RemindTaskInfo info = new RemindTaskInfo();
        info.setId(task.getId());
        info.setIsSend(true);
        info.setActualTime(LocalDateTime.now());
        // 发送消息
        remindTaskInfoService.updateById(info);
        RemindTaskInfo one = remindTaskInfoService.getOne(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getRemindTaskId,
                        task.getOtherId())
                .eq(RemindTaskInfo::getIsSend, false)
                .orderByAsc(RemindTaskInfo::getEstimatedTime)
                .last("limit 1"));
        putRemindTask(one.getId(), one.getRemindTaskId(), one.getEstimatedTime(), one.getRemindType(),
                task.getCommonMethod(), task.getOtherMap());
    }

    private String getRemindMsg(int i, String name) {
        switch (i) {
            case 1 -> {
                return "尊敬的用户，您的任务“<strong> {任务名称} </strong>”已到达设定时间，请及时处理。".replace(
                        "{任务名称}", name);
            }
            case 2 -> {
                return "嘿，您的任务“<strong> {任务名称} </strong>”时间到啦！快去完成吧~".replace("{任务名称}", name);
            }
            case 3 -> {
                return "哎呀，时间老人敲门啦！您的任务“<strong> {任务名称} </strong>”该行动了，别让机会溜走哦！".replace(
                        "{任务名称}", name);
            }
            case 4 -> {
                return "嗨，您的任务“<strong> {任务名称} </strong>”快要到期了！请及时处理。".replace("{任务名称}", name);
            }
            case 5 -> {
                return "任务“<strong> {任务名称} </strong>”时间到！请处理。".replace("{任务名称}", name);
            }
            case 6 -> {
                return "现在是完成任务“<strong> {任务名称} </strong>”的最佳时刻！加油，您一定行！".replace("{任务名称}",
                        name);
            }
            case 7 -> {
                return "系统提示：任务“<strong> {任务名称} </strong>”已到达指定时间，请立即执行操作。".replace(
                        "{任务名称}", name);
            }
            case 8 -> {
                return "时光流转，此刻正是完成“<strong> {任务名称} </strong>”的良辰。愿您在这一刻书写完美的篇章。".replace(
                        "{任务名称}", name);
            }
            default -> {
                return "您的任务“<strong> {任务名称} </strong>”已到达设定时间，请及时处理。".replace("{任务名称}", name);
            }
        }
    }
}
