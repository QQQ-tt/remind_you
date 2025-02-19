package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("remind_task_info")
@Schema(name = "RemindTaskInfo", description = "任务执行详情数据")
public class RemindTaskInfo extends BaseEntity {

    @TableField("remind_task_id")
    private Long remindTaskId;

    @Schema(description = "预计发送时间")
    @TableField("estimated_time")
    private LocalDateTime estimatedTime;

    @Schema(description = "实际发送时间")
    @TableField("actual_time")
    private LocalDateTime actualTime;

    @Schema(description = "执行时间")
    @TableField("time")
    private LocalDateTime time;

    @Schema(description = "是否提醒")
    @TableField("is_remind")
    private Boolean isRemind;

    @Schema(description = "提醒方式")
    @TableField("remind_type")
    private RemindTypeEnum remindType;

    @Schema(description = "是否发送")
    @TableField("is_send")
    private Boolean isSend;

    @Schema(description = "是否已读")
    @TableField("is_read")
    private Boolean isRead;
}
