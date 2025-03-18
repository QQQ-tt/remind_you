package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 请求日志
 * </p>
 *
 * @author QQQtx
 * @since 2025-03-11
 */
@Getter
@Setter
@TableName("request_log")
@Schema(name = "RequestLog", description = "请求日志")
public class RequestLog extends BaseEntity {

    @Schema(description = "请求ip")
    @TableField("ip")
    private String ip;

    @Schema(description = "请求地址")
    @TableField("url")
    private String url;

    @Schema(description = "请求类型")
    @TableField("method")
    private String method;

    @Schema(description = "请求时间")
    @TableField("time")
    private LocalDateTime time;

    @Schema(description = "请求时长（毫秒数）")
    @TableField("size")
    private Integer size;

    @Schema(description = "HTTP 状态码")
    @TableField("status_code")
    private String statusCode;

    @Schema(description = "响应体大小（字节）")
    @TableField("response_length")
    private Integer responseLength;

    @Schema(description = "客户端浏览器和操作系统信息")
    @TableField("user_agent")
    private String userAgent;

    @Schema(description = "请求参数")
    @TableField("params")
    private String params;

    @Schema(description = "自定义请求头")
    @TableField("headers")
    private String headers;
}
