package com.health.remind.common.enums;

/**
 * @author QQQtx
 * @since 2025/2/10
 */
public enum FrequencySqlTypeEnum {

    INSERT("insert"),
    SELECT("select");

    private final String string;

    FrequencySqlTypeEnum(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
