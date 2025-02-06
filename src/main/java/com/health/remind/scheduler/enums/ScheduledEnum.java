package com.health.remind.scheduler.enums;

import lombok.Getter;

/**
 * @author QQQtx
 * @since 2025/2/6 13:28
 */
@Getter
public enum ScheduledEnum {

    TEST(1, "测试"),

    DELAY_SCHEDULED(2, "延时任务"),

    DELAY_ATTENUATION_SCHEDULED(3, "衰减轮询任务"),

    ;

    private final Integer sort;

    private final String name;

    ScheduledEnum(Integer sort, String name) {
        this.sort = sort;
        this.name = name;
    }
}
