package com.health.remind.service;

import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 任务执行详情数据 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-27
 */
public interface RemindTaskInfoService extends IService<RemindTaskInfo> {

    /**
     * 将任务详情添加到队列中
     *
     * @param remindTask 任务
     */
    void putTask(RemindTask remindTask);

    /**
     * 初始化任务详情添加到队列中
     */
    void initTask();

    /**
     * 根据id初始化任务详情添加到队列中
     *
     * @param id 任务id
     */
    void initTaskById(Long id);
}
