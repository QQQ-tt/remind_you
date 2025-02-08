package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 异常日志log
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-08
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("exception_log")
@Schema(name = "ExceptionLog", description = "异常日志log")
public class ExceptionLog extends BaseEntity {

    @Schema(description = "异常名称")
    @TableField("exception_name")
    private String exceptionName;

    @Schema(description = "请求地址")
    @TableField("url")
    private String url;

    @Schema(description = "请求参数")
    @TableField("parameter")
    private String parameter;

    @Schema(description = "异常等级")
    @TableField("level")
    private Integer level;

    @Schema(description = "异常信息")
    @TableField("message")
    private String message;

    @Schema(description = "堆栈信息")
    @TableField("stack_trace")
    private String stackTrace;
}
