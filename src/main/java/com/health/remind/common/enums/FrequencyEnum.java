package com.health.remind.common.enums;

import lombok.Getter;

/**
 * enum('hour', 'day', 'week', 'month', 'year')
 * @author QQQtx
 * @since 2025/2/5 13:47
 */
@Getter
public enum FrequencyEnum {
    HOUR("hour", "小时"),
    DAY("day", "天"),
    WEEK("week", "周"),
    MONTH("month", "月"),
    YEAR("year", "年");

    private final String value;
    private final String desc;

    FrequencyEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
