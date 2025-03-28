package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.mapper.RemindTaskInfoMapper;
import com.health.remind.pojo.dto.RemindTaskInfoPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.scheduler.DelayScheduledExecutor;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import com.health.remind.service.RemindTaskInfoService;
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

    private final ThreadPoolExecutor threadPoolExecutor;

    public RemindTaskInfoServiceImpl(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public Page<RemindTaskInfoVO> pageTaskInfo(RemindTaskInfoPageDTO dto) {
        return baseMapper.selectPageTaskInfo(dto.getPage(),
                Wrappers.lambdaQuery(RemindTaskInfo.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(dto.getId() != null, RemindTaskInfo::getId, dto.getId())
                        .eq(RemindTaskInfo::getRemindTaskId, dto.getRemindTaskId())
                        .orderByAsc(RemindTaskInfo::getEstimatedTime));
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
                                e.getEstimatedTime(), e.getRemindType(), map,
                                Map.of(RemindTypeEnum.remind_email.toString(), task.getEmail(), "NAME",
                                        task.getName())));
            }, threadPoolExecutor);
        }
    }

    @PostConstruct
    @Override
    public void initTask() {
        CompletableFuture.runAsync(() -> {
            Map<Long, List<RemindTaskInfo>> listMap = list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getIsRemind, true)
                    .eq(RemindTaskInfo::getStatus, true)
                    .eq(RemindTaskInfo::getIsSend, false)
                    .ne(RemindTaskInfo::getEstimatedTime, LocalDateTime.now())
                    .orderByAsc(RemindTaskInfo::getEstimatedTime)
                    .orderByAsc(RemindTaskInfo::getRemindTaskId)).stream()
                    .collect(Collectors.groupingBy(RemindTaskInfo::getRemindTaskId));
            putTask(listMap);
        }, threadPoolExecutor);
    }

    @Override
    public void initTaskById(Long id) {
        CompletableFuture.runAsync(() -> {
            Map<Long, List<RemindTaskInfo>> listMap = list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getRemindTaskId, id)
                    .eq(RemindTaskInfo::getIsRemind, true)
                    .eq(RemindTaskInfo::getStatus, true)
                    .eq(RemindTaskInfo::getIsSend, false)
                    .ne(RemindTaskInfo::getEstimatedTime, LocalDateTime.now())
                    .orderByAsc(RemindTaskInfo::getEstimatedTime)
                    .orderByAsc(RemindTaskInfo::getRemindTaskId)).stream()
                    .collect(Collectors.groupingBy(RemindTaskInfo::getRemindTaskId));
            putTask(listMap);
        }, threadPoolExecutor);
    }

    private void putTask(Map<Long, List<RemindTaskInfo>> listMap) {
        listMap.forEach((k, v) -> v.stream()
                .findFirst()
                .ifPresent(e -> {
                    CommonMethod.setTenantId(e.getTenantId()
                            .toString());
                    DelayScheduledExecutor.putRemindTask(e.getId(), e.getRemindTaskId(),
                            e.getEstimatedTime(),
                            e.getRemindType(), CommonMethod.getMap(),
                            Map.of(RemindTypeEnum.remind_email.toString(), e.getEmail(), "NAME",
                                    e.getRemindTaskName()));
                }));
    }

    @Override
    public Integer listDelayTaskError() {
        return baseMapper.selectTaskError();
    }
}
