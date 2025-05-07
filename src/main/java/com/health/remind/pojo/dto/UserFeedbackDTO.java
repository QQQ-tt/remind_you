package com.health.remind.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/5/7 16:33
 */
@Data
public class UserFeedbackDTO {

    @NotNull
    private Long id;

    @NotNull
    private Integer adopted;

    private String reply;
}
