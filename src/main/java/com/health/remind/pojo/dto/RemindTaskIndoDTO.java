package com.health.remind.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/11
 */
@Data
public class RemindTaskIndoDTO {

    @NotNull(message = "startTime不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "endTime不能为空")
    private LocalDateTime endTime;
}
