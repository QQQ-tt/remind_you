package com.health.remind.pojo.vo;

import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/5 13:06
 */
@Data
public class FrequencyVO {

    private Long id;

    @Schema(description = "频次名称")
    private String frequencyName;

    @Schema(description = "频次编码")
    private String frequencyCode;

    @Schema(description = "频次描述")
    private String frequencyDesc;

    @Schema(description = "频次数目")
    private Integer frequencyNumber;

    @Schema(description = "频次周期")
    private Integer frequencyCycle;

    @Schema(description = "周期单位")
    private FrequencyEnum cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    private FrequencyTypeEnum type;

    @Schema(description = "是否启用")
    private Boolean status;
}
