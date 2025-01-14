package com.health.remind.config.enums;

import lombok.Getter;

/**
 * @author qtx
 * @since 2025/1/14 10:15
 */
@Getter
public enum DataEnums {

    /**
     * 成功
     */
    SUCCESS("成功", 200),
    /**
     * 失败
     */
    FAILED("失败", 500),
    /**
     * 入参数据异常
     */
    DATA_IS_ABNORMAL("入参数据异常", 205);

    /**
     * 提示
     */
    private final String msg;
    /**
     * 错误编码
     */
    private final int code;

    DataEnums(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
