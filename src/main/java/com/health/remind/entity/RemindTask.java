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
 * 提醒任务
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Getter
@Setter
@TableName("remind_task")
@Schema(name = "RemindTask", description = "提醒任务")
public class RemindTask extends BaseEntity {

    @Schema(description = "任务名称")
    @TableField("name")
    private String name;

    @Schema(description = "提醒类型:1单次,2多次,3无限")
    @TableField("type")
    private Integer type;

    @Schema(description = "单次提醒触发时间")
    @TableField("remind_time")
    private LocalDateTime remindTime;

    @Schema(description = "多次提醒:开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "多次提醒:结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(description = "提醒次数")
    @TableField("num")
    private Integer num;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "是否提醒")
    @TableField("is_remind")
    private Boolean isRemind;

    @Schema(description = "提醒方式")
    @TableField("remind_type")
    private Integer remindType;

    @Schema(description = "提前时间的数量")
    @TableField("advance_num")
    private Integer advanceNum;

    @Schema(description = "单位:分钟,小时,天,周")
    @TableField("unit")
    private String unit;

    @TableField("frequency_id")
    private Long frequencyId;
}
