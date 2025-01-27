package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("frequency")
@Schema(name = "Frequency", description = "频率")
public class Frequency extends BaseEntity {

    @Schema(description = "频次名称")
    @TableField("frequency_name")
    private String frequencyName;

    @Schema(description = "频次数目")
    @TableField("frequency_number")
    private Integer frequencyNumber;

    @Schema(description = "频次周期")
    @TableField("frequency_cycle")
    private Integer frequencyCycle;

    @Schema(description = "周期单位")
    @TableField("cycle_unit")
    private String cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    @TableField("type")
    private Integer type;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @TableField(exist = false)
    private List<FrequencyDetail> frequencyDetailList;
}
