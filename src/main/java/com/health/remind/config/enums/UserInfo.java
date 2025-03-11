package com.health.remind.config.enums;

/**
 * @author QQQtx
 * @since 2024/11/26 11:11
 */
public enum UserInfo {
    USER_ID("user_id"),
    USER_NAME("user_name"),
    TOKEN("Authorization"),
    TENANT_ID("tenant_id"),
    IP("ip"),
    URL("url"),
    METHOD("method"),
    PARAMETER("parameter");

    public final String msg;

    UserInfo(String string) {
        msg = string;
    }

    @Override
    public String toString() {
        return msg;
    }
}
