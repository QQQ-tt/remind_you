package com.health.remind.config.enums;

/**
 * @author QQQtx
 * @since 2024/11/26 11:11
 */
public enum UserInfo {
    USER_ID("user_id"), USER_NAME("user_name"), TOKEN("token"), tenant_id("tenant_id");

    public final String msg;

    UserInfo(String string) {
        msg = string;
    }

    @Override
    public String toString() {
        return msg;
    }
}
