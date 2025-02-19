package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysResourcePageDTO extends PageDTO<SysResource> {
}
