package com.health.remind.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/11
 */
@Data
public class RemindTaskIndoDTO {

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
