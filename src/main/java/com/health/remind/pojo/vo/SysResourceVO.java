package com.health.remind.pojo.vo;

import com.health.remind.common.enums.SysResourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysResourceVO {

    private Long id;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "接口地址/路由地址")
    private String url;

    @Schema(description = "资源类型：路由、接口")
    private SysResourceEnum type;

    @Schema(description = "方法类型")
    private String method;

    @Schema(description = "详细描述")
    private String description;

    @Schema(description = "资源状态")
    private Boolean status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    private Long parentId;

    private List<SysResourceVO> children;
}
