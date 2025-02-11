package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.health.remind.common.enums.FrequencySqlTypeEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.mapper.RemindTaskMapper;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskIndoDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.service.FrequencyDetailService;
import com.health.remind.service.FrequencyService;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import com.health.remind.strategy.FrequencyUtils;
import com.health.remind.util.RedisUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    public RemindTaskServiceImpl(RemindTaskInfoService remindTaskInfoService, FrequencyService frequencyService, FrequencyDetailService frequencyDetailService, FrequencyUtils frequencyUtils) {
        this.remindTaskInfoService = remindTaskInfoService;
        this.frequencyService = frequencyService;
        this.frequencyDetailService = frequencyDetailService;
        this.frequencyUtils = frequencyUtils;
    }

    @Override
    public Page<RemindTaskVO> pageTaskByUserId(RemindTaskPageDTO dto) {
        return baseMapper.selectPageTask(dto.getPage(),
                Wrappers.lambdaQuery(RemindTask.class)
                        .eq(RemindTask::getCreateId, CommonMethod.getUserId())
                        .like(StringUtils.isNotBlank(dto.getName()), RemindTask::getName, dto.getName())
                        .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public RemindTaskVO getTaskById(Long id) {
        return baseMapper.selectOneById(id, CommonMethod.getUserId());
    }

    @Override
    public boolean saveOrUpdateTask(RemindTaskDTO task) {
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
                // 频率不可修改
                .frequencyId(task.getId() == null ? task.getFrequencyId() : null)
                .build();
        boolean b = saveOrUpdate(build);
        if (task.getId() == null) {
            frequencyUtils.splitTask(build, FrequencySqlTypeEnum.INSERT);
            Set<String> keys = RedisUtils.keys(RedisKeys.getRemindInfoKey(CommonMethod.getUserId(), null, null));
            RedisUtils.delete(keys);
        }
        return b;
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
    public List<RemindTaskInfoVO> getTaskInfoByUserId(RemindTaskIndoDTO dto) {
        String remindInfoKey = RedisKeys.getRemindInfoKey(CommonMethod.getUserId(), dto.getStartTime(),
                dto.getEndTime());
        boolean flag = RedisUtils.hasKey(remindInfoKey);
        if (flag) {
            return RedisUtils.getObject(remindInfoKey, new TypeReference<>() {
            });
        }
        List<RemindTask> list = list(Wrappers.lambdaQuery(RemindTask.class)
                .eq(BaseEntity::getCreateId, CommonMethod.getUserId())
                .ge(RemindTask::getStartTime, dto.getStartTime())
                .le(RemindTask::getEndTime, dto.getEndTime()));
        if (list.isEmpty()) {
            return List.of();
        }
        Map<Long, RemindTask> taskMap = list.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        List<RemindTaskInfoVO> taskInfoVOS = remindTaskInfoService.list(Wrappers.lambdaQuery(RemindTaskInfo.class)
                        .in(RemindTaskInfo::getRemindTaskId,
                                list.stream()
                                        .map(BaseEntity::getId)
                                        .toList()))
                .stream()
                .map(m -> RemindTaskInfoVO.builder()
                        .name(taskMap.get(m.getRemindTaskId())
                                .getName())
                        .time(m.getTime())
                        .isRead(m.getIsRead())
                        .build())
                .toList();
        RedisUtils.setObject(remindInfoKey, taskInfoVOS);
        return taskInfoVOS;
    }

    @Override
    public boolean removeTask(Long id) {
        if (removeById(id)) {
            remindTaskInfoService.remove(Wrappers.lambdaQuery(RemindTaskInfo.class)
                    .eq(RemindTaskInfo::getRemindTaskId, id));
        }
        return false;
    }

    private void saveFrequency(RemindTaskDTO task) {
        if (task.getFrequencyId() != null) {
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
