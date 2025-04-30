package com.health.remind.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/4/30 10:14
 */
@Data
public class AppUserDTO {

    private String nickname;

    private String avatar;

    @NotNull
    private Integer authorized;
}
