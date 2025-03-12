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

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "执行时间")
    private LocalDateTime time;

    @Schema(description = "是否已读")
    private Boolean isRead;

    @Schema(description = "是否已发送")
    private Boolean isSend;
}
