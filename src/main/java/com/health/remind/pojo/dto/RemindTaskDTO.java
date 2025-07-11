package com.health.remind.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.health.remind.common.enums.FrequencyEnum;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/5 13:03
 */
@Data
public class RemindTaskDTO {

    private Long id;

    @Size(max = 20, message = "备注长度不能超过20个字符")
    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "时间范围提醒:开始时间")
    private LocalDateTime startTime;

    @Schema(description = "时间范围提醒:结束时间")
    private LocalDateTime endTime;

    @Size(max = 50, message = "备注长度不能超过50个字符")
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否提醒")
    private Boolean isRemind;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "提醒方式")
    private RemindTypeEnum remindType = RemindTypeEnum.none;

    @Size(max = 6, message = "验证码长度不能超过6个字符")
    @Schema(description = "验证码")
    private String captchaCode;

    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Size(max = 11, message = "手机号长度不能超过11个字符")
    @Schema(description = "手机号")
    @TableField(value = "telephone")
    private String telephone;

    @Schema(description = "提前时间的数量")
    private Integer advanceNum;

    @Schema(description = "提前时间单位:分钟,小时,天,周")
    private FrequencyEnum cycleUnit;

    @Schema(description = "频率id")
    private Long frequencyId;

    @Schema(description = "自定义频率")
    private FrequencyDTO frequency;

    @Schema(description = "自定义频率详情")
    private FrequencyDetailDTO frequencyDetail;
}
