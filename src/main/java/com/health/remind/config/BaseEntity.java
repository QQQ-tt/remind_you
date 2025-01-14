package com.health.remind.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qtx
 * @since 2024/7/23 10:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT, value = "create_id")
    private Long createId;

    @TableField(fill = FieldFill.INSERT, value = "create_name")
    private String createName;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE, value = "update_id")
    private Long updateId;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE, value = "update_name")
    private String updateName;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE, value = "update_time")
    private LocalDateTime updateTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT, value = "tenant_id")
    private Long tenantId;

    @JsonIgnore
    @TableLogic
    private Boolean deleteFlag;

    public BaseEntity(Long id) {
        this.id = id;
    }
}
