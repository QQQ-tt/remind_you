package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.RemindTask;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskVO;

/**
 * <p>
 * 提醒任务 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
public interface RemindTaskService extends IService<RemindTask> {

    /**
     * 分页查询用户提醒任务
     *
     * @param dto 查询条件
     * @return 分页集合
     */
    Page<RemindTaskVO> pageTaskByUserId(RemindTaskPageDTO dto);

    /**
     * 获取任务详情
     *
     * @param id 任务id
     * @return 任务详情
     */
    RemindTaskVO getTaskById(Long id);

    /**
     * 保存或更新任务
     *
     * @param task 任务
     * @return 是否保存成功
     */
    boolean saveOrUpdateTask(RemindTaskDTO task);

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return 是否删除成功
     */
    boolean removeTask(Long id);

}
