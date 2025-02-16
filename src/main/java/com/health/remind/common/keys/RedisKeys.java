package com.health.remind.common.keys;

import java.time.LocalDateTime;

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
    public static String getRemindInfoKey(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return "remind:info:" + userId + ":*";
        }
        return "remind:info:" + userId + ":" + startTime + "-" + endTime;
    }

    /**
     * 获取登录key
     *
     * @param account 账号
     * @return login:account
     */
    public static String getLoginKey(String account) {
        if (account == null) {
            return "login:*";
        }
        return "login:" + account;
    }
}
