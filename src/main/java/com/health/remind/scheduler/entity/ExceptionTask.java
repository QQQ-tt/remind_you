package com.health.remind.scheduler.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author QQQtx
 * @since 2025/2/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionTask {

    @Schema(description = "异常名称")
    private String exceptionName;

    @Schema(description = "异常等级")
    private Integer level;

    @Schema(description = "异常信息")
    private String message;

    @Schema(description = "堆栈信息")
    private String stackTrace;
}
