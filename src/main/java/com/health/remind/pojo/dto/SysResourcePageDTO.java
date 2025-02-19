package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysResourcePageDTO extends PageDTO<SysResource> {

    @Schema(description = "资源名称")
    private String name;
}
