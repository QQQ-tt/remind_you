package com.health.remind.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/2/14 22:09
 */
@Data
@AllArgsConstructor
public class SignVO {

    @Schema(description = "账户")
    private Long account;
}
