package com.health.remind.pojo.dto;

import com.health.remind.common.enums.UserFeedbackEnum;
import com.health.remind.config.PageDTO;
import com.health.remind.entity.UserFeedback;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qtx
 * @since 2025/5/7 16:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserFeedbackPageDTO extends PageDTO<UserFeedback> {

    private UserFeedbackEnum type;

    private String problem;

    private Integer adopted;
}
