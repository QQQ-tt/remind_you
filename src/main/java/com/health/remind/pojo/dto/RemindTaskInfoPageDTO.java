package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.RemindTaskInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/3/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RemindTaskInfoPageDTO extends PageDTO<RemindTaskInfo> {

    private Long id;

    @NotBlank(message = "提醒任务id不能为空")
    private String remindTaskId;
}
