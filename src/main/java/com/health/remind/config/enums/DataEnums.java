package com.health.remind.config.enums;

import lombok.Getter;

/**
 * @author QQQtx
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
     * 用户未登录
     */
    USER_NOT_LOGIN("用户未登录", 403),
    /**
     * 密码错误
     */
    PASSWORD_ERROR("密码错误", 403),
    /**
     * 入参数据异常
     */
    DATA_IS_ABNORMAL("入参数据异常", 205),
    /**
     * 数据重复
     */
    DATA_REPEAT("数据重复", 206),
    /**
     * 系统繁忙
     */
    SYSTEM_BUSY("系统繁忙", 500);

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
