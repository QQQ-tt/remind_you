package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.Frequency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/5 13:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FrequencyPageDTO extends PageDTO<Frequency> {

    @Schema(description = "频次名称")
    private String frequencyName;
}
