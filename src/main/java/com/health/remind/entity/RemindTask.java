package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.config.BaseEntity;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@AllArgsConstructor
@NoArgsConstructor
@TableName("remind_task")
@Schema(name = "RemindTask", description = "提醒任务")
public class RemindTask extends BaseEntity {

    @Schema(description = "任务名称")
    @TableField("name")
    private String name;

    @Schema(description = "时间范围提醒:开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "时间范围提醒:结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(description = "推送次数")
    @TableField("push_num")
    private Integer pushNum;

    @Schema(description = "提醒次数(计算得出)")
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
    private RemindTypeEnum remindType;

    @Schema(description = "提前时间的数量")
    @TableField("advance_num")
    private Integer advanceNum;

    @Schema(description = "提前时间单位:分钟,小时,天,周")
    @TableField("cycle_unit")
    private FrequencyEnum cycleUnit;

    @TableField("frequency_id")
    private Long frequencyId;

    @TableField(exist = false)
    private LocalDate initTime;

    @Builder
    public RemindTask(Long id, Long createId, String createName, LocalDateTime createTime, Long updateId, String updateName, LocalDateTime updateTime, Long tenantId, Boolean deleteFlag, String name, LocalDateTime startTime, LocalDateTime endTime, Integer pushNum, Integer num, String remark, Boolean isRemind, RemindTypeEnum remindType, Integer advanceNum, FrequencyEnum cycleUnit, Long frequencyId) {
        super(id, createId, createName, createTime, updateId, updateName, updateTime, tenantId, deleteFlag);
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pushNum = pushNum;
        this.num = num;
        this.remark = remark;
        this.isRemind = isRemind;
        this.remindType = remindType;
        this.advanceNum = advanceNum;
        this.cycleUnit = cycleUnit;
        this.frequencyId = frequencyId;
    }
}
