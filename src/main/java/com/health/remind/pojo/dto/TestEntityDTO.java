package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author QQQtx
 * @since 2025/1/14 13:17
 */
@Getter
@Setter
public class TestEntityDTO {

    private Long id;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "数量")
    private Integer num;
}
