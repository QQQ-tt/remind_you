package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
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

    @Size(max = 10, message = "name不能大于10")
    @Schema(description = "名字")
    private String name;

    @Max(value = 100, message = "num不能大于100")
    @Schema(description = "数量")
    private Integer num;
}
