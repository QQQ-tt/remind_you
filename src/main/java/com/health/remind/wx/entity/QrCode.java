package com.health.remind.wx.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/7/2 11:21
 */
@Data
public class QrCode {

    @Schema(description = "小程序页面")
    private String page;

    @Schema(description = "小程序页面参数")
    private String scene;
}
