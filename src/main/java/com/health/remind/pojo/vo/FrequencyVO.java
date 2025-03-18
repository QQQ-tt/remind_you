package com.health.remind.pojo.vo;

import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/2/5 13:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyVO {

    private Long id;

    @Schema(description = "频次名称")
    private String name;

    @Schema(description = "频次编码")
    private String frequencyCode;

    @Schema(description = "频次描述")
    private String frequencyDesc;

    @Schema(description = "执行次数")
    private Integer frequencyNumber;

    @Schema(description = "频次周期")
    private Integer frequencyCycle;

    @Schema(description = "周期单位")
    private FrequencyEnum cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    private FrequencyTypeEnum type;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "频次详情")
    private List<FrequencyDetailVO> frequencyDetailList;
}
