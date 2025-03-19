package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.mapper.RemindTaskInfoMapper;
import com.health.remind.scheduler.DelayScheduledExecutor;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务执行详情数据 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-27
 */
@Service
public class RemindTaskInfoServiceImpl extends ServiceImpl<RemindTaskInfoMapper, RemindTaskInfo> implements RemindTaskInfoService {

    private final RemindTaskService remindTaskService;

    private final ThreadPoolExecutor threadPoolExecutor;

    public RemindTaskInfoServiceImpl(RemindTaskService remindTaskService, ThreadPoolExecutor threadPoolExecutor) {
        this.remindTaskService = remindTaskService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void putTask(RemindTask task) {
        Map<UserInfo, String> map = CommonMethod.getMap();
        if (task.getIsRemind()) {
            CompletableFuture.runAsync(() -> {
                CommonMethod.setMap(map);
                List<RemindTaskInfo> list = list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                        .eq(RemindTaskInfo::getRemindTaskId, task.getId())
                        .eq(RemindTaskInfo::getIsSend, false)
                        .orderByAsc(RemindTaskInfo::getEstimatedTime));
                // 只放第一个任务
                list.stream()
                        .findFirst()
                        .ifPresent(e -> DelayScheduledExecutor.putRemindTask(e.getId(), task.getId(),
                                e.getEstimatedTime(), e.getRemindType(), map));
            }, threadPoolExecutor);
        }
    }

    @PostConstruct
    @Override
    public void initTask() {
        CompletableFuture.runAsync(() -> {
            List<RemindTask> taskList = remindTaskService.list(Wrappers.lambdaQuery(RemindTask.class)
                    .eq(RemindTask::getStatus, Boolean.TRUE));
            Map<Long, List<RemindTaskInfo>> listMap = list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getIsRemind, true)
                    .eq(RemindTaskInfo::getIsSend, false)
                    .ne(RemindTaskInfo::getEstimatedTime, LocalDateTime.now())
                    .in(RemindTaskInfo::getRemindTaskId, taskList.stream()
                            .map(BaseEntity::getId)
                            .toList())
                    .orderByAsc(RemindTaskInfo::getEstimatedTime)
                    .orderByAsc(RemindTaskInfo::getRemindTaskId)).stream()
                    .collect(Collectors.groupingBy(RemindTaskInfo::getRemindTaskId));
            listMap.forEach((k, v) -> v.stream()
                    .findFirst()
                    .ifPresent(e -> {
                        CommonMethod.setTenantId(e.getTenantId()
                                .toString());
                        DelayScheduledExecutor.putRemindTask(e.getId(), e.getRemindTaskId(),
                                e.getEstimatedTime(),
                                e.getRemindType(), CommonMethod.getMap());
                    }));
        }, threadPoolExecutor);
    }
}
