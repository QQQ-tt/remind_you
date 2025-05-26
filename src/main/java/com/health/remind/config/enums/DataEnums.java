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
    USER_NOT_LOGIN("用户未登录", 401),
    /**
     * 用户角色异常
     */
    USER_ROLE_ERROR("用户角色异常", 403),
    /**
     * 用户资源不足
     */
    USER_RESOURCE_ERROR("用户资源不足", 403),
    /**
     * 用户状态异常
     */
    USER_STATUS_ERROR("请联系管理员",403),
    /**
     * TOKEN异常
     */
    USER_TOKEN_ERROR("TOKEN异常", 401),
    /**
     * 密码错误
     */
    PASSWORD_ERROR("密码错误", 401),
    /**
     * 入参数据异常
     */
    DATA_IS_ABNORMAL("入参数据异常", 400),
    /**
     * 校验失败
     */
    VERIFICATION_FAILED("校验失败", 422),
    /**
     * 数据重复
     */
    DATA_REPEAT("数据重复", 209),
    /**
     * 数据不存在
     */
    DATA_NOT_EXIST("数据不存在", 201),
    /**
     * 系统繁忙
     */
    SYSTEM_BUSY("系统繁忙", 429);

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
