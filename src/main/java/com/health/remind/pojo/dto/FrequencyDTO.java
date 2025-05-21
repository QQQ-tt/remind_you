package com.health.remind.pojo.dto;

import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author QQQtx
 * @since 2025/2/5 13:59
 */
@Data
public class FrequencyDTO {

    private Long id;

    @NotBlank(message = "频次名称不能为空")
    @Schema(description = "频次名称")
    private String frequencyName;

    @NotBlank(message = "频次编码不能为空")
    @Schema(description = "频次编码")
    private String frequencyCode;

    @Schema(description = "频次描述")
    private String frequencyDesc;

    @Min(value = 1, message = "频次数目不能小于1")
    @Schema(description = "频次数目")
    private Integer frequencyNumber;

    @Min(value = 1, message = "频次周期不能小于1")
    @Schema(description = "频次周期")
    private Integer frequencyCycle;

    @Schema(description = "周期单位")
    private FrequencyEnum cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    private FrequencyTypeEnum type = FrequencyTypeEnum.UNKNOWN;

    @Schema(description = "是否启用")
    private Boolean status = Boolean.FALSE;

    @Schema(description = "等级")
    private int level = 1;

    @Schema(description = "开始时间(小时类型设置)")
    private LocalTime startTime;

    @Schema(description = "结束时间(小时类型设置)")
    private LocalTime endTime;

    @Schema(description = "是否跨天")
    private Boolean crossDay;

    @Schema(description = "来源")
    private String source = "sys";


}
