package com.health.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.UserFeedback;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.pojo.dto.UserFeedbackDTO;
import com.health.remind.pojo.dto.UserFeedbackPageDTO;

import java.util.List;

/**
 * <p>
 * 意见反馈表 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-07
 */
public interface UserFeedbackService extends IService<UserFeedback> {

    /**
     * 分页查询意见反馈
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    Page<UserFeedback> pageUserFeedback(UserFeedbackPageDTO dto);

    /**
     * 根据用户id查询意见反馈
     *
     * @return 意见反馈列表
     */
    List<UserFeedback> listUserFeedbackByUser();

    /**
     * 保存意见反馈
     *
     * @param userFeedback 意见反馈对象
     * @return 是否保存成功
     */
    boolean saveUserFeedback(UserFeedback userFeedback);

    /**
     * 处理意见反馈
     *
     * @param dto 意见反馈对象
     * @return 是否处理成功
     */
    boolean handlingComments(UserFeedbackDTO dto);

}
