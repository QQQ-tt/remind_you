package com.health.remind.test.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author QQQtx
 * @since 2025/3/25
 */
@Slf4j
public class DateTest {

    @Test
    public void test() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
                .plusDays(2);
        log.info("开始时间:{},结束时间:{}", startTime, endTime);
        for (; startTime.isBefore(endTime); startTime = startTime.plusDays(1)) {
            log.info("时间:{}", startTime.toLocalDate());
        }
    }

    @Test
    public void test1() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .withDayOfMonth(3)
                .withHour(4)
                .withMinute(0)
                .withSecond(0)
                .plusMonths(1);
        log.info("时间:{}", localDateTime);
    }
}
