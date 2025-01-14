package com.health.remind.pay.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 衰减时间间隔，总计30分钟
 *
 * @author qtx
 * @since 2025/1/14 17:20
 */
@Getter
public enum TimeEnum {

    MINUTE_0(0, 0),
    MINUTE_1(1, 1),
    MINUTE_2(2, 2),
    MINUTE_3(3, 3),
    MINUTE_10(10, 4),
    MINUTE_15(15, 5),
    ;

    private final int minute;
    private final int sort;

    TimeEnum(int minute, int sort) {
        this.minute = minute;
        this.sort = sort;
    }

    @Getter
    private static final Map<Integer, Integer> map = new HashMap<>();

    static {
        Stream.of(TimeEnum.values())
                .forEach(e -> map.put(e.sort, e.minute));
    }

    public static Integer getSort(Integer sort) {
        return map.get(sort);
    }
}
