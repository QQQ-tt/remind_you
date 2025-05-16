package com.health.remind.pojo.dto;

import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qtx
 * @since 2025/5/16 15:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRulePageDTO extends PageDTO<SysUser> {

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "权益等级：vip_0,vip_1 ")
    private InterestsLevelEnum interestsLevel;
}
