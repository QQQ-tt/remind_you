package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 测试表
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("test")
@Schema(name = "Test", description = "测试表")
public class Test extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("old_name")
    private String oldName;

    @TableField("num")
    private Integer num;

    @TableField("all_num")
    private Integer allNum;
}
