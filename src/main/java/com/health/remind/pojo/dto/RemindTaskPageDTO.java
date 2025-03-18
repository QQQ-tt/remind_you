package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.RemindTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/5 13:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RemindTaskPageDTO extends PageDTO<RemindTask> {

    @Schema(description = "任务名称")
    private String name;
}
