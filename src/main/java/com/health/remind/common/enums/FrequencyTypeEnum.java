package com.health.remind.common.enums;

import lombok.Getter;

/**
 * 0自然周,1逻辑周
 *
 * @author QQQtx
 * @since 2025/2/5 14:12
 */
@Getter
public enum FrequencyTypeEnum {

    UNKNOWN(-1, "未使用"),
    NATURAL_WEEK(0, "自然周"),
    LOGIC_WEEK(1, "逻辑周");

    private final Integer type;

    private final String desc;

    FrequencyTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String toString() {
        return type.toString();
    }
}
