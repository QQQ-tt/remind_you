package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 任务执行详情数据
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-27
 */
@Getter
@Setter
@TableName("remind_task_info")
@Schema(name = "RemindTaskInfo", description = "任务执行详情数据")
public class RemindTaskInfo extends BaseEntity {

    @TableField("remind_task_id")
    private Long remindTaskId;

    @Schema(description = "执行时间")
    @TableField("time")
    private LocalDateTime time;

    @Schema(description = "是否提醒")
    @TableField("is_remind")
    private Boolean isRemind;

    @Schema(description = "是否已读")
    @TableField("is_read")
    private Boolean isRead;
}
