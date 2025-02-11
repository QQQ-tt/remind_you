package com.health.remind.pojo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2025/2/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemindInfoExcelVO {

    @ExcelProperty("任务名称")
    private String name;

    @ExcelProperty("提醒时间")
    private LocalDateTime time;
}
