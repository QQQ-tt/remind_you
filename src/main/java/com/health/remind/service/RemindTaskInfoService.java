package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.RemindTask;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.pojo.dto.RemindTaskInfoDTO;
import com.health.remind.pojo.dto.RemindTaskInfoPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;

import java.util.List;

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
     * 分页获取任务详情
     *
     * @param dto 查询条件
     * @return 分页集合
     */
    Page<RemindTaskInfoVO> pageTaskInfo(RemindTaskInfoPageDTO dto);

    /**
     * 获取任务详情
     *
     * @param dto 查询条件
     * @return 任务详情集合
     */
    List<RemindTaskInfoVO> listTaskInfo(RemindTaskInfoDTO dto);

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

    /**
     * 定时任务执行时间误差
     *
     * @return 秒
     */
    Integer listDelayTaskError();
}
