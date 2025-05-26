package com.health.remind.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author qtx
 * @since 2025/2/14 21:55
 */
public class NumUtils {

    /**
     * 生成size个start到end之间的整数
     *
     * @param size  长度
     * @param start 区间开始数字
     * @param end   区间结束数字
     * @return 数字字符串
     */
    public static long numRandom(int size, int start, int end) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(random.nextInt(end - start) + start);
        }
        return Long.parseLong(stringBuilder.toString());
    }

    /**
     * 6位随机数
     */
    public static String numRandom6() {
        return String.valueOf(numRandom(1, 1, 9)) + numRandom(5, 0, 9);
    }

    /**
     * 随机生成用户card
     *
     * @return 数字字符串
     */
    public static long numUserCard() {
        return Long.parseLong(numRandom(1, 1, 9) + String.valueOf(numRandom(9, 0, 9)));
    }

    /**
     * 生成uuid,不带斜杠
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toLowerCase();
    }
}
