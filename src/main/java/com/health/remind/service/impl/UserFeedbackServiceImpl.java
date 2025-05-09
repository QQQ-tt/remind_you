package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.config.CommonMethod;
import com.health.remind.entity.UserFeedback;
import com.health.remind.mapper.UserFeedbackMapper;
import com.health.remind.pojo.dto.UserFeedbackDTO;
import com.health.remind.pojo.dto.UserFeedbackPageDTO;
import com.health.remind.service.UserFeedbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 意见反馈表 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-07
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements UserFeedbackService {

    @Override
    public Page<UserFeedback> pageUserFeedback(UserFeedbackPageDTO dto) {
        return page(dto.getPage(), Wrappers.lambdaQuery(UserFeedback.class)
                .like(StringUtils.isNotBlank(dto.getTitle()), UserFeedback::getTitle, dto.getTitle())
                .eq(dto.getType() != null, UserFeedback::getType, dto.getType())
                .eq(dto.getAdopted() != null, UserFeedback::getAdopted, dto.getAdopted())
                .like(StringUtils.isNotBlank(dto.getContent()), UserFeedback::getContent, dto.getContent())
                .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public List<UserFeedback> listUserFeedbackByUser() {
        return list(Wrappers.lambdaQuery(UserFeedback.class)
                .orderByDesc(BaseEntity::getCreateTime)
                .eq(BaseEntity::getCreateId, CommonMethod.getAccount())
                .ge(BaseEntity::getCreateTime,
                        LocalDateTime.now()
                                .minusMonths(1L)));
    }

    @Override
    public boolean saveUserFeedback(UserFeedback userFeedback) {
        return save(userFeedback);
    }

    @Override
    public boolean handlingComments(UserFeedbackDTO dto) {
        UserFeedback feedback = new UserFeedback();
        feedback.setId(dto.getId());
        feedback.setReply(dto.getReply());
        feedback.setAdopted(dto.getAdopted());
        return updateById(feedback);
    }
}
