package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.UserFeedback;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/5/7 16:29
 */
@Data
public class UserFeedbackPageDTO extends PageDTO<UserFeedback> {

    private String title;

    private Integer type;

    private String content;

    private Integer adopted;
}
