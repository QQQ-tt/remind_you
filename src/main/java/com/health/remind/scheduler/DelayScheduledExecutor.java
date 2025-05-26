package com.health.remind.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.entity.SysUser;
import com.health.remind.mail.MailService;
import com.health.remind.scheduler.entity.DelayTask;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import com.health.remind.scheduler.enums.ScheduledEnum;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import com.health.remind.service.SysUserService;
import com.health.remind.util.RedisUtils;
import com.health.remind.wx.WxApiService;
import com.health.remind.wx.entity.MsgInfo;
import com.health.remind.wx.entity.WxMsg;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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

    private final SysUserService sysUserService;

    private final WxApiService wxApiService;

    public DelayScheduledExecutor(ScheduledExecutorService scheduler, RemindTaskService remindTaskService, RemindTaskInfoService remindTaskInfoService, MailService mailService, SysUserService sysUserService, WxApiService wxApiService) {
        this.scheduler = scheduler;
        this.remindTaskService = remindTaskService;
        this.remindTaskInfoService = remindTaskInfoService;
        this.mailService = mailService;
        this.sysUserService = sysUserService;
        this.wxApiService = wxApiService;
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
        log.debug("放入任务:{},任务类型:{},执行时间:{},是否存在:{},otherMap:{}", taskId, remindTypeEnum, executeTime,
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

    @Transactional(rollbackFor = Exception.class)
    public void processTasks() {
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

    @RedisLock(lockParameter = "#task.id", retryNum = 0)
    public void executeTask(DelayTask task) {
        log.debug("执行任务:{},任务类型:{}", task.getId(), task.getRemindTypeEnum());
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
        // 发送消息
        updateStatus(task);
    }

    private void remindEmailTask(DelayTask task) {
        // 发送消息
        Optional.ofNullable(task.getOtherMap())
                .ifPresent(map -> {
                    if (map.containsKey(RemindTypeEnum.remind_email.toString()) && map.containsKey("NAME")) {
                        mailService.send(map.get(RemindTypeEnum.remind_email.toString()), "提醒",
                                getRemindMsg(0, map.get("NAME")));
                    }
                });
        updateStatus(task);
    }

    private void remindWxTask(DelayTask task) {
        boolean update = sysUserService.update(Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getAccount, CommonMethod.getAccount())
                .ne(SysUser::getMsgNum, 0)
                .setSql("msg_num = msg_num - 1"));
        String taskKey = RedisKeys.getTaskKey(CommonMethod.getAccount(), Long.parseLong(task.getOtherId()));
        RemindTask remindTask = RedisUtils.getObject(taskKey, RemindTask.class);
        if (remindTask == null) {
            remindTask = remindTaskService.getById(Long.parseLong(task.getOtherId()));
            String openId = sysUserService.getOne(Wrappers.lambdaQuery(SysUser.class)
                            .eq(SysUser::getAccount,
                                    CommonMethod.getAccount()))
                    .getOpenId();
            if (openId == null) {
                return;
            }
            remindTask.setOpenId(openId);
        }
        if (remindTask.getEndTime() == null) {
            remindTask.setEndTime(LocalDateTime.now());
        }
        if (update) {
            WxMsg wxMsg = new WxMsg();
            wxMsg.setTemplate_id("ahi62RYx-WStwOclzC26ZEBaSgZNaVWjs_eyuFefWzM");
            wxMsg.setTouser(remindTask.getOpenId());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            wxMsg.setData(
                    Map.of("thing7", new MsgInfo(remindTask.getName()), "thing9", new MsgInfo(remindTask.getRemark()),
                            "time11", new MsgInfo(pattern.format(now)),
                            "date6", new MsgInfo(remindTask.getEndTime()
                                    .toLocalDate()
                                    .toString())));
            wxApiService.sendMsg(wxMsg);
        } else {
            return;
        }
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
        remindTaskInfoService.updateById(info);
        long count = remindTaskInfoService.count(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getRemindTaskId,
                        task.getOtherId())
                .eq(RemindTaskInfo::getIsSend, true));
        remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                .eq(BaseEntity::getId, task.getOtherId())
                .set(RemindTask::getPushNum, count));
        RemindTaskInfo one = remindTaskInfoService.getOne(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getRemindTaskId,
                        task.getOtherId())
                .eq(RemindTaskInfo::getIsSend, false)
                .orderByAsc(RemindTaskInfo::getEstimatedTime)
                .last("limit 1"));
        if (one == null) {
            remindTaskService.update(Wrappers.lambdaUpdate(RemindTask.class)
                    .eq(BaseEntity::getId, task.getOtherId())
                    .set(RemindTask::getIsFinish, true));
            return;
        }
        putRemindTask(one.getId(), one.getRemindTaskId(), one.getEstimatedTime(), one.getRemindType(),
                task.getCommonMethod(), task.getOtherMap());
    }

    private String getRemindMsg(int i, String name) {
        if (i == 0) {
            Random random = new Random();
            i = random.nextInt(8) + 1;
        }
        switch (i) {
            case 1 -> {
                return "尊敬的用户，您的任务:“<strong> {任务名称} </strong>”已到达设定时间，请及时处理。".replace(
                        "{任务名称}", name);
            }
            case 2 -> {
                return "嘿，您的任务:“<strong> {任务名称} </strong>”时间到啦！快去完成吧~".replace("{任务名称}", name);
            }
            case 3 -> {
                return "哎呀，时间老人敲门啦！您的任务:“<strong> {任务名称} </strong>”该行动了，别让机会溜走哦！".replace(
                        "{任务名称}", name);
            }
            case 4 -> {
                return "嗨，您的任务:“<strong> {任务名称} </strong>”快要到期了！请及时处理。".replace("{任务名称}", name);
            }
            case 5 -> {
                return "您的任务:“<strong> {任务名称} </strong>”时间到了！请处理。".replace("{任务名称}", name);
            }
            case 6 -> {
                return "现在是完成任务:“<strong> {任务名称} </strong>”的最佳时刻！加油，您一定行！".replace("{任务名称}",
                        name);
            }
            case 7 -> {
                return "系统提示：您的任务:“<strong> {任务名称} </strong>”已到达指定时间，请立即执行操作。".replace(
                        "{任务名称}", name);
            }
            case 8 -> {
                return "时光流转，此刻正是完成:“<strong> {任务名称} </strong>”的良辰。愿您在这一刻书写完美的篇章。".replace(
                        "{任务名称}", name);
            }
            default -> {
                return "您的任务:“<strong> {任务名称} </strong>”已到达设定时间，请及时处理。".replace("{任务名称}", name);
            }
        }
    }
}
