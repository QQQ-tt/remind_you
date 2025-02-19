package com.health.remind.pojo.vo;

import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/5 13:01
 */
@Data
public class RemindTaskVO {

    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "多次提醒:开始时间")
    private LocalDateTime startTime;

    @Schema(description = "多次提醒:结束时间")
    private LocalDateTime endTime;

    @Schema(description = "推送次数")
    private Integer pushNum;

    @Schema(description = "提醒次数(计算得出)")
    private Integer num;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否提醒")
    private Boolean isRemind;

    @Schema(description = "提醒方式")
    private RemindTypeEnum remindType;

    @Schema(description = "提前时间的数量")
    private Integer advanceNum;

    @Schema(description = "提前时间单位:分钟,小时,天,周")
    private FrequencyEnum cycleUnit;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "频率id")
    private Long frequencyId;

    @Schema(description = "频率名称")
    private String frequencyName;


}
