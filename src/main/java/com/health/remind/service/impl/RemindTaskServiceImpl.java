package com.health.remind.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.RemindTask;
import com.health.remind.mapper.RemindTaskMapper;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.service.RemindTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public Page<RemindTaskVO> pageTaskByUserId(RemindTaskPageDTO dto) {
        return null;
    }

    @Override
    public boolean saveOrUpdateTask(RemindTaskDTO task) {
        return false;
    }

    @Override
    public boolean removeTask(Long id) {
        return false;
    }
}
