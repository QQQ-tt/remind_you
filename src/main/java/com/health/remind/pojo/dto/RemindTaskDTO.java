package com.health.remind.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.health.remind.common.enums.FrequencyEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/5 13:03
 */
@Data
public class RemindTaskDTO {

    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "时间范围提醒:开始时间")
    private LocalDateTime startTime;

    @Schema(description = "时间范围提醒:结束时间")
    private LocalDateTime endTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否提醒")
    private Boolean isRemind;

    @Schema(description = "提醒方式")
    private Integer remindType;

    @Schema(description = "提前时间的数量")
    private Integer advanceNum;

    @Schema(description = "提前时间单位:分钟,小时,天,周")
    private FrequencyEnum cycleUnit;

    @Schema(description = "频率id")
    private Long frequencyId;

    @Schema(description = "自定义频率")
    private FrequencyDTO frequency;

    @Schema(description = "自定义频率详情")
    private FrequencyDetailDTO frequencyDetail;
}
