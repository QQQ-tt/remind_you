package com.health.remind.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleVO {

    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色状态")
    private Boolean status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
