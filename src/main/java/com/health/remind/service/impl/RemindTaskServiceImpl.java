package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencySqlTypeEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.Frequency;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.entity.SysUser;
import com.health.remind.excel.ExcelTransfer;
import com.health.remind.mail.MailService;
import com.health.remind.mapper.RemindTaskMapper;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskIndoDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindInfoExcelVO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.scheduler.ScheduledBase;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import com.health.remind.scheduler.enums.ScheduledEnum;
import com.health.remind.service.FrequencyDetailService;
import com.health.remind.service.FrequencyService;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import com.health.remind.strategy.FrequencyUtils;
import com.health.remind.util.RedisUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 提醒任务 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Service
public class RemindTaskServiceImpl extends ServiceImpl<RemindTaskMapper, RemindTask> implements RemindTaskService {

    private final RemindTaskInfoService remindTaskInfoService;

    private final FrequencyService frequencyService;

    private final FrequencyDetailService frequencyDetailService;

    private final FrequencyUtils frequencyUtils;

    private final MailService mailService;

    private final ObjectMapper objectMapper;

    public RemindTaskServiceImpl(RemindTaskInfoService remindTaskInfoService, FrequencyService frequencyService, FrequencyDetailService frequencyDetailService, FrequencyUtils frequencyUtils, MailService mailService, ObjectMapper objectMapper) {
        this.remindTaskInfoService = remindTaskInfoService;
        this.frequencyService = frequencyService;
        this.frequencyDetailService = frequencyDetailService;
        this.frequencyUtils = frequencyUtils;
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<RemindTaskVO> pageTask(RemindTaskPageDTO dto) {
        return baseMapper.selectPageTask(dto.getPage(),
                Wrappers.lambdaQuery(RemindTask.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .like(StringUtils.isNotBlank(dto.getName()), RemindTask::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public Page<RemindTaskVO> pageTaskByUserId(RemindTaskPageDTO dto) {
        return baseMapper.selectPageTask(dto.getPage(),
                Wrappers.lambdaQuery(RemindTask.class)
                        .eq(BaseEntity::getDeleteFlag, false)
                        .eq(RemindTask::getCreateId, CommonMethod.getAccount())
                        .like(StringUtils.isNotBlank(dto.getName()), RemindTask::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public RemindTaskVO getTaskById(Long id) {
        return baseMapper.selectOneById(id, CommonMethod.getAccount());
    }

    @SneakyThrows
    @Override
    @RedisLock(lockParameter = "T(com.health.remind.config.CommonMethod).getAccount()")
    public boolean saveOrUpdateTask(RemindTaskDTO task) {
        if (task.getId() != null) {
            RemindTask byId = getById(task.getId());
            if (byId == null) {
                throw new DataException(DataEnums.DATA_NOT_EXIST);
            }
        }
        if (task.getRemindType()
                .equals(RemindTypeEnum.remind_email)) {
            if (StringUtils.isBlank(task.getEmail())) {
                throw new DataException(DataEnums.DATA_NOT_EXIST, "邮箱不存在。");
            }
            String emailCode = RedisKeys.getEmailCode(CommonMethod.getAccount(), task.getEmail());
            String s = RedisUtils.get(emailCode);
            if (StringUtils.isBlank(s) || !s.equals(task.getCaptchaCode())) {
                throw new DataException(DataEnums.DATA_NOT_EXIST, "验证码错误。");
            }
            RedisUtils.delete(emailCode);
        }
        saveFrequency(task);
        RemindTask build = RemindTask.builder()
                .id(task.getId())
                .name(task.getName())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .remark(task.getRemark())
                .status(task.getStatus())
                .isRemind(task.getIsRemind())
                .remindType(task.getRemindType())
                .email(task.getEmail())
                .telephone(task.getTelephone())
                .advanceNum(task.getAdvanceNum())
                .cycleUnit(task.getCycleUnit())
                // 频率不可修改
                .frequencyId(task.getId() == null ? task.getFrequencyId() : null)
                .build();
        boolean b = saveOrUpdate(build);
        if (task.getId() == null) {
            frequencyUtils.splitTask(build, FrequencySqlTypeEnum.INSERT);
            remindTaskInfoService.putTask(build);
        }
        Set<String> keys = RedisUtils.keys(RedisKeys.getRemindInfoKey(CommonMethod.getAccount(), null));
        RedisUtils.delete(keys);
        setRedis(build);
        return b;
    }

    @Override
    public void setRedis(RemindTask build) throws JsonProcessingException {
        Boolean b1 = RedisUtils.hasKey(RedisKeys.getUserKey(CommonMethod.getAccount(), StaticConstant.USER_TYPE_APP));
        if (b1) {
            Optional.ofNullable(RedisUtils.getObject(
                            RedisKeys.getUserKey(CommonMethod.getAccount(), StaticConstant.USER_TYPE_APP),
                            new TypeReference<SysUser>() {
                            }))
                    .ifPresent(object -> build.setOpenId(object.getOpenId()));
        }
        String taskKey = RedisKeys.getTaskKey(CommonMethod.getAccount(), build.getId());
        RedisUtils.set(taskKey, objectMapper.writeValueAsString(build));
        if (build.getEndTime() != null) {
            LocalDateTime endTime = build.getEndTime();
            Duration duration = Duration.between(LocalDateTime.now(), endTime);
            RedisUtils.expire(taskKey, duration.getSeconds(), TimeUnit.SECONDS);
        } else {
            RedisUtils.persist(taskKey);
        }
    }

    @Override
    public void sendEmailCode(String email) {
        mailService.sendCode(email);
    }

    @Override
    public boolean resetTask(Long id) {
        RemindTask byId = getById(id);
        Frequency frequency =
                Optional.ofNullable(frequencyService.getById(byId.getFrequencyId()))
                        .orElseThrow(() -> new DataException(DataEnums.DATA_NOT_EXIST, "频率以下架"));
        if (frequency.getCycleUnit()
                .equals(FrequencyEnum.HOUR_MANUAL)) {
            List<RemindTaskInfo> list = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getRemindTaskId, id)
                    .eq(RemindTaskInfo::getIsSend, false));
            if (!list.isEmpty()) {
                remindTaskInfoService.update(Wrappers.lambdaUpdate(RemindTaskInfo.class)
                        .setSql("status = !status")
                        .in(BaseEntity::getId, list.stream()
                                .map(BaseEntity::getId)
                                .toList()));
                list.forEach(e -> ScheduledBase.cancelTask(e.getId(), ScheduledEnum.DELAY_SCHEDULED, false));
            }
            frequencyUtils.splitTask(byId, FrequencySqlTypeEnum.INSERT);
            remindTaskInfoService.putTask(byId);
            Set<String> keys = RedisUtils.keys(RedisKeys.getRemindInfoKey(CommonMethod.getAccount(), null));
            RedisUtils.delete(keys);
            return update(Wrappers.lambdaUpdate(RemindTask.class)
                    .eq(BaseEntity::getId, id)
                    .set(RemindTask::getIsFinish, false));
        }
        return false;
    }

    @Override
    public Boolean updateStatus(Long id) {
        boolean update = update(Wrappers.lambdaUpdate(RemindTask.class)
                .setSql("status = !status")
                .eq(BaseEntity::getId, id));
        remindTaskInfoService.update(Wrappers.lambdaUpdate(RemindTaskInfo.class)
                .setSql("status = !status")
                .eq(RemindTaskInfo::getRemindTaskId, id));
        List<RemindTaskInfo> list = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getRemindTaskId, id)
                .eq(RemindTaskInfo::getIsSend, false));
        list.stream()
                .findAny()
                .ifPresent(i -> {
                    if (i.getStatus()) {
                        remindTaskInfoService.initTaskById(id);
                    } else {
                        list.forEach(e -> ScheduledBase.cancelTask(e.getId(), ScheduledEnum.DELAY_SCHEDULED, false));
                    }
                });
        return update;
    }

    @Override
    public List<LocalDateTime> testTaskInfoNumTen(RemindTaskDTO task) {
        saveFrequency(task);
        RemindTask build = RemindTask.builder()
                .id(task.getId())
                .name(task.getName())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .remark(task.getRemark())
                .isRemind(task.getIsRemind())
                .remindType(task.getRemindType())
                .advanceNum(task.getAdvanceNum())
                .cycleUnit(task.getCycleUnit())
                .frequencyId(task.getFrequencyId())
                .build();
        return frequencyUtils.splitTask(build, FrequencySqlTypeEnum.SELECT)
                .stream()
                .map(RemindTaskInfo::getTime)
                .toList();
    }

    @Override
    public void exportTaskInfo(Long id, HttpServletResponse response) {
        RemindTask byId = getById(id);
        List<RemindTaskInfo> list = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getRemindTaskId, id));
        List<RemindInfoExcelVO> excelList = list.stream()
                .map(m -> new RemindInfoExcelVO(byId.getName(), m.getTime()))
                .toList();
        ExcelTransfer.exportExcel(response, excelList, "任务详情", byId.getName(), RemindInfoExcelVO.class);
    }

    @Override
    public Map<LocalDate, List<RemindTaskInfoVO>> getTaskInfoByUserId(RemindTaskIndoDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        String remindInfoKey = RedisKeys.getRemindInfoKey(CommonMethod.getAccount(), now.toLocalDate());
        boolean flag = RedisUtils.hasKey(remindInfoKey);
        if (flag) {
            return RedisUtils.getObject(remindInfoKey, new TypeReference<>() {
            });
        }
        List<RemindTask> list = list(Wrappers.lambdaQuery(RemindTask.class)
                .eq(BaseEntity::getCreateId, CommonMethod.getAccount())
                .eq(RemindTask::getStatus, true)
                .eq(RemindTask::getIsFinish, false)
                .le(dto.getEndTime() == null && dto.getStartTime() == null, RemindTask::getStartTime, now)
                .ge(dto.getStartTime() != null, RemindTask::getStartTime, dto.getStartTime())
                .le(dto.getEndTime() != null, RemindTask::getEndTime, dto.getEndTime()));
        if (list.isEmpty()) {
            return Map.of();
        }
        Map<Long, RemindTask> taskMap = list.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        List<RemindTaskInfoVO> taskInfoVOS = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                        .in(RemindTaskInfo::getRemindTaskId,
                                list.stream()
                                        .map(BaseEntity::getId)
                                        .toList())
                        .orderByAsc(RemindTaskInfo::getEstimatedTime))
                .stream()
                .map(m -> RemindTaskInfoVO.builder()
                        .name(taskMap.get(m.getRemindTaskId())
                                .getName())
                        .time(m.getTime())
                        .estimatedTime(m.getEstimatedTime())
                        .isRead(m.getIsRead())
                        .isSend(m.getIsSend())
                        .build())
                .toList();
        LinkedHashMap<LocalDate, List<RemindTaskInfoVO>> map = taskInfoVOS.stream()
                .sorted(Comparator.comparing(RemindTaskInfoVO::getTime))
                .collect(Collectors.groupingBy(e -> e.getTime()
                        .toLocalDate(), LinkedHashMap::new, Collectors.toList()));
        RedisUtils.setObject(remindInfoKey, map);
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        RedisUtils.expire(remindInfoKey, now.toLocalTime()
                .until(endOfDay, ChronoUnit.SECONDS), TimeUnit.SECONDS);
        return map;
    }

    @Override
    public boolean removeTaskById(Long id) {
        boolean b = removeById(id);
        if (b) {
            List<RemindTaskInfo> list = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getRemindTaskId, id)
                    .eq(RemindTaskInfo::getIsRead, false));
            remindTaskInfoService.remove(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getRemindTaskId, id));
            list.forEach(e -> ScheduledBase.cancelTask(e.getId(), ScheduledEnum.DELAY_SCHEDULED, false));
        }
        return b;
    }

    private void saveFrequency(RemindTaskDTO task) {
        if (task.getFrequencyId() != null || task.getId() != null) {
            return;
        }
        if (task.getFrequency() == null) {
            throw new DataException(DataEnums.DATA_IS_ABNORMAL, "自定义频率不能为空");
        }
        frequencyService.saveOrUpdateFrequency(task.getFrequency());
        task.setFrequencyId(task.getFrequency()
                .getId());
        FrequencyDetailDTO frequencyDetail = task.getFrequencyDetail();
        if (frequencyDetail != null) {
            frequencyDetail.setFrequencyId(task.getFrequencyId());
            frequencyDetailService.saveOrUpdateFrequencyDetail(frequencyDetail);
        }
    }
}
