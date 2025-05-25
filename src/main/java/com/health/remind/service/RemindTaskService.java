package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.RemindTask;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskIndoDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.pojo.vo.RemindTaskVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    Page<RemindTaskVO> pageTask(RemindTaskPageDTO dto);

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
     * 发送邮箱验证码
     */
    void sendEmailCode(String email);

    /**
     * 重置任务
     *
     * @param id 提醒任务id
     * @return 是否重置成功
     */
    boolean resetTask(Long id);

    /**
     * 更改任务状态
     *
     * @param id 任务id
     * @return 是否更改成功
     */
    Boolean updateStatus(Long id);

    /**
     * 测试任务获取10条提醒时间
     *
     * @param task 任务详情
     * @return 10条提醒时间
     */
    List<LocalDateTime> testTaskInfoNumTen(RemindTaskDTO task);

    /**
     * 导出对应任务的时间详情
     *
     * @param id       任务id
     * @param response r
     */
    void exportTaskInfo(Long id, HttpServletResponse response);

    /**
     * 获取用户提醒任务详情
     *
     * @param dto 查询条件
     * @return 任务详情
     */
    Map<LocalDate, List<RemindTaskInfoVO>> getTaskInfoByUserId(RemindTaskIndoDTO dto);

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return 是否删除成功
     */
    boolean removeTaskById(Long id);
}
