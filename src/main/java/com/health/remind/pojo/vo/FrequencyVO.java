package com.health.remind.pojo.vo;

import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.common.enums.FrequencyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private Double frequencyCycle;

    @Schema(description = "周期单位")
    private FrequencyEnum cycleUnit;

    @Schema(description = "开始方式:0自然周,1逻辑周")
    private FrequencyTypeEnum type;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "开始时间(小时类型设置)")
    private LocalTime startTime;

    @Schema(description = "结束时间(小时类型设置)")
    private LocalTime endTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "跨天执行(手动类小时类型设置)")
    private Boolean crossDay;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "频次详情")
    private List<FrequencyDetailVO> frequencyDetailList;
}
