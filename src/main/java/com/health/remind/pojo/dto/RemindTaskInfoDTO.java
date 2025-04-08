package com.health.remind.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/4/8
 */
@Data
public class RemindTaskInfoDTO {

    @NotBlank(message = "提醒任务id不能为空")
    private String remindTaskId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
