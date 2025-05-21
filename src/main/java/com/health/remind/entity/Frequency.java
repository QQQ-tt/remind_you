package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 频率
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("frequency")
@Schema(name = "Frequency", description = "频率")
public class Frequency extends BaseEntity {

    @Schema(description = "频次名称")
    @TableField("name")
    private String name;

    @Schema(description = "频次编码")
    @TableField("frequency_code")
    private String frequencyCode;

    @Schema(description = "频次描述")
    @TableField("frequency_desc")
    private String frequencyDesc;

    @Schema(description = "频次数目")
    @TableField("frequency_number")
    private Integer frequencyNumber;

    @Schema(description = "频次周期")
    @TableField("frequency_cycle")
    private Integer frequencyCycle;

    @Schema(description = "周期单位")
    @TableField("cycle_unit")
    private FrequencyEnum cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    @TableField("type")
    private FrequencyTypeEnum type;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "来源")
    @TableField("source")
    private String source;

    @Schema(description = "等级")
    @TableField("level")
    private Integer level;

    @Schema(description = "开始时间(小时类型设置)")
    @TableField("start_time")
    private LocalTime startTime;

    @Schema(description = "结束时间(小时类型设置)")
    @TableField("end_time")
    private LocalTime endTime;

    @Schema(description = "跨天执行(手动类小时类型设置)")
    @TableField("cross_day")
    private Boolean crossDay;

    @TableField(exist = false)
    private List<FrequencyDetail> frequencyDetailList;

    @Builder
    public Frequency(Long id, Long createId, String createName, LocalDateTime createTime, Long updateId,
                     String updateName, LocalDateTime updateTime, Long tenantId, Boolean deleteFlag,
                     String frequencyName, String frequencyCode, String frequencyDesc, Integer frequencyNumber,
                     Integer frequencyCycle, FrequencyEnum cycleUnit, FrequencyTypeEnum type, Boolean status,
                     String source, Integer level,LocalTime startTime,LocalTime endTime) {
        super(id, createId, createName, createTime, updateId, updateName, updateTime, tenantId, deleteFlag);
        this.name = frequencyName;
        this.frequencyCode = frequencyCode;
        this.frequencyDesc = frequencyDesc;
        this.frequencyNumber = frequencyNumber;
        this.frequencyCycle = frequencyCycle;
        this.cycleUnit = cycleUnit;
        this.type = type;
        this.status = status;
        this.source = source;
        this.level = level;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
