package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "1.bug；2.意见")
    @TableField("type")
    private Integer type;

    @Schema(description = "具体内容")
    @TableField("content")
    private String content;

    @Schema(description = "是否采纳")
    @TableField("adopted")
    private Integer adopted;

    @Schema(description = "回复")
    @TableField("reply")
    private String reply;
}
