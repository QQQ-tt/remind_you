package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.mapper.RemindTaskMapper;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.service.FrequencyDetailService;
import com.health.remind.service.FrequencyService;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RemindTaskService;
import org.springframework.stereotype.Service;

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

    public RemindTaskServiceImpl(RemindTaskInfoService remindTaskInfoService, FrequencyService frequencyService, FrequencyDetailService frequencyDetailService) {
        this.remindTaskInfoService = remindTaskInfoService;
        this.frequencyService = frequencyService;
        this.frequencyDetailService = frequencyDetailService;
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
        boolean b = saveOrUpdate(RemindTask.builder()
                .id(task.getId())
                .name(task.getName())
                .type(task.getType())
                .remindTime(task.getRemindTime())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .remark(task.getRemark())
                .isRemind(task.getIsRemind())
                .remindType(task.getRemindType())
                .advanceNum(task.getAdvanceNum())
                .cycleUnit(task.getCycleUnit())
                // 频率不可修改
                .frequencyId(task.getId() == null ? task.getFrequencyId() : null)
                .build());
        return b;
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
