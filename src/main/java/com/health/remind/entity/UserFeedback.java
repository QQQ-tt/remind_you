package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.common.enums.UserFeedbackEnum;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 意见反馈表
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-07
 */
@Getter
@Setter
@TableName("user_feedback")
@Schema(name = "UserFeedback", description = "意见反馈表")
public class UserFeedback extends BaseEntity {

    @Size(max = 100, message = "联系方式长度不能超过100个字符")
    @Schema(description = "联系方式")
    @TableField("contact")
    private String contact;

    @NotNull(message = "请选择反馈类型")
    @Schema(description = "1.suggestion 建议；2.issue 问题")
    @TableField("type")
    private UserFeedbackEnum type;

    @Size(max = 1000, message = "内容长度不能超过1000个字符")
    @Schema(description = "具体内容")
    @TableField("problem")
    private String problem;

    @Schema(description = "是否采纳0待处理1已处理（未采纳）2已采纳")
    @TableField("adopted")
    private Integer adopted;

    @Schema(description = "回复")
    @TableField("reply")
    private String reply;
}
