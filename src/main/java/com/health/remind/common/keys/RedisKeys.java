package com.health.remind.common.keys;

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
}
