package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.FrequencyDetail;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/5 14:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FrequencyDetailPageDTO extends PageDTO<FrequencyDetail> {

    @NotNull(message = "frequencyId不能为空")
    private Long frequencyId;
}
