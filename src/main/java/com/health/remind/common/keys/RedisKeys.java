package com.health.remind.common.keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author QQQtx
 * @since 2025/2/5 15:38
 */
public class RedisKeys {

    /**
     * 获取frequency_detail:userId
     *
     * @param userId 用户id
     * @return frequency_detail:userId
     */
    public static String getFrequencyAllKey(Long userId) {
        if (userId == null) {
            return "frequency:frequency_all:*";
        }
        return "frequency:frequency_all:" + userId;
    }

    /**
     * 时间范围内查询
     *
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return remind:info:userId:startTime-endTime
     */
    public static String getRemindInfoKey(Long userId, LocalDate startTime) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startTime == null) {
            return "remind:info:" + userId + ":*";
        }
        return "remind:info:" + userId + ":" + pattern.format(startTime);
    }

    /**
     * 获取登录key
     *
     * @param account 账号
     * @param type    类型
     * @return login:account
     */
    public static String getLoginKey(String account, String type) {
        if (type == null) {
            return "login:*";
        }
        if (account == null) {
            return "login:" + type + ":*";
        }
        return "login:" + type + ":" + account;
    }

    /**
     * 获取token权限key
     *
     * @param appName    app名称
     * @param moduleName 模块名称
     * @return permission:appName:moduleName
     */
    public static String getTokenPermissionKey(String appName, String moduleName) {
        if (moduleName == null) {
            return "permission:" + appName + ":*";
        }
        return "permission:" + appName + ":" + moduleName;
    }

    /**
     * 获取角色资源key
     *
     * @param roleId 角色id
     * @return role:roleId
     */
    public static String getRoleResourceKey(Long roleId) {
        if (roleId == null) {
            return "role:*";
        }
        return "role:" + roleId;
    }
}
