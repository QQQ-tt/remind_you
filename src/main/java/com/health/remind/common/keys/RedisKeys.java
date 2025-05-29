package com.health.remind.common.keys;

import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;

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
            return "frequency:info:*";
        }
        return "frequency:info:" + userId;
    }

    /**
     * 时间范围内查询
     *
     * @param startTime 开始时间
     * @return remind:info:userId:startTime-endTime
     */
    public static String getRemindInfoKey(Long account, LocalDate startTime) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startTime == null) {
            return "remind:info:" + account + ":*";
        }
        return "remind:info:" + account + ":" + pattern.format(startTime);
    }

    /**
     * 获取任务key
     *
     * @param account 账号
     * @return remind:task:account
     */
    public static String getTaskKey(Long account, Long taskId) {
        if (account == null) {
            throw new DataException(DataEnums.DATA_NOT_EXIST);
        }
        if (taskId == null) {
            return "remind:task:" + account + ":*";
        }
        return "remind:task:" + account + ":" + taskId;
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

    public static String getUserKey(Long account, String type) {
        if (account == null) {
            throw new DataException(DataEnums.DATA_NOT_EXIST);
        }
        return "user:" + type + ":" + account;
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

    /**
     * 获取规则用户key
     *
     * @param account 账号
     * @return rule:user:account
     */
    public static String getRuleUser(Long account, String type) {
        if (account == null) {
            throw new DataException(DataEnums.DATA_NOT_EXIST);
        }
        return "rule:user:" + type + ":" + account;
    }

    /**
     * 获取邮箱验证码key
     *
     * @param account 账号
     * @param email   邮箱
     * @return email:code:account:email
     */
    public static String getEmailCode(Long account, String email) {
        if (email == null) {
            throw new DataException(DataEnums.DATA_NOT_EXIST);
        }
        return "email:code:" + account + ":" + email;
    }
}
