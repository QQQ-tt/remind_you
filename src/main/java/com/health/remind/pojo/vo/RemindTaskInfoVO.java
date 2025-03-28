package com.health.remind.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemindTaskInfoVO {

    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "预计发送时间")
    private LocalDateTime estimatedTime;

    @Schema(description = "实际发送时间")
    private LocalDateTime actualTime;

    @Schema(description = "执行时间")
    private LocalDateTime time;

    @Schema(description = "是否已发送")
    private Boolean isSend;

    @Schema(description = "是否已读")
    private Boolean isRead;
}
