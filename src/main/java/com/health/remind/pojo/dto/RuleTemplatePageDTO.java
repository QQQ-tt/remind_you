package com.health.remind.pojo.dto;

import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.config.PageDTO;
import com.health.remind.entity.RuleTemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qtx
 * @since 2025/5/14 12:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RuleTemplatePageDTO extends PageDTO<RuleTemplate> {

    @Schema(description = "规则名称")
    private String name;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "权益等级：vip_0,vip_1,ad_boost")
    private InterestsLevelEnum interestsLevel;
}
