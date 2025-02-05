package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.RemindTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/5 13:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RemindTaskPageDTO extends PageDTO<RemindTask> {
}
