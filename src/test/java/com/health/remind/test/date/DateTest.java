package com.health.remind.test.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    @Test
    public void test2() {
        int i = 4;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextFour = LocalDateTime.of(LocalDate.now(), LocalTime.of(i, 0));
        // 如果当前时间在4点之后，则取明天的4点；否则取今天的4点
        if (now.getHour() > 4 || (now.getHour() == 4 && now.getMinute() > 0)) {
            nextFour = nextFour.plusDays(1);
        }
        log.info("时间:{}", nextFour);
    }

    @Test
    public void test3() {
        log.info(LocalDate.now()
                .toString());
    }

    @Test
    public void test4() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusHours(13);
        log.info("时间1:{}", pattern.format(now));
        LocalDateTime localDateTime1 = localDateTime.plusHours(13);
        log.info("时间2:{}", localDateTime1);
        LocalDateTime localDateTime2 = localDateTime1.plusHours(13);
        log.info("时间3:{}", localDateTime2);
        LocalDateTime localDateTime3 = localDateTime2.plusHours(13);
        log.info("时间4:{}", localDateTime3);
        LocalDateTime localDateTime4 = localDateTime3.plusHours(13);
        log.info("时间5:{}", localDateTime4);
    }

    @Test
    public void test5() {
        int i = 13;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime nextTime = getNextTime(LocalDateTime.now());
        log.info("时间:{}", nextTime.format(pattern));
        LocalDateTime nextTime1 = getNextTime(nextTime.plusHours(i));
        log.info("时间:{}", nextTime1.format(pattern));
        LocalDateTime nextTime2 = getNextTime(nextTime1.plusHours(i));
        log.info("时间:{}", nextTime2.format(pattern));
        LocalDateTime nextTime3 = getNextTime(nextTime2.plusHours(i));
        log.info("时间:{}", nextTime3.format(pattern));
        LocalDateTime nextTime4 = getNextTime(nextTime3.plusHours(i));
        log.info("时间:{}", nextTime4.format(pattern));
        LocalDateTime nextTime5 = getNextTime(nextTime4.plusHours(i));
        log.info("时间:{}", nextTime5.format(pattern));
        LocalDateTime nextTime6 = getNextTime(nextTime5.plusHours(i));
        log.info("时间:{}", nextTime6.format(pattern));
        LocalDateTime nextTime7 = getNextTime(nextTime6.plusHours(i));
        log.info("时间:{}", nextTime7.format(pattern));
        LocalDateTime nextTime8 = getNextTime(nextTime7.plusHours(i));
        log.info("时间:{}", nextTime8.format(pattern));
        LocalDateTime nextTime9 = getNextTime(nextTime8.plusHours(i));
        log.info("时间:{}", nextTime9.format(pattern));
        LocalDateTime nextTime10 = getNextTime(nextTime9.plusHours(i));
        log.info("时间:{}", nextTime10.format(pattern));
    }

    public LocalDateTime getNextTime(LocalDateTime now) {
        if (now.isBefore(LocalDateTime.of(now.toLocalDate(), LocalTime.of(8, 0)))) {
            return LocalDateTime.of(now.toLocalDate(), LocalTime.of(8, 0));
        }
        return now;
    }

    @Test
    public void test6() {
        int i = 13;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        log.info("时间1:{}", now.format(pattern));
        LocalDateTime localDateTime = now.plusHours(i);
        log.info("时间2:{}", localDateTime.format(pattern));
        LocalDateTime localDateTime1 = localDateTime.plusHours(i);
        log.info("时间3:{}", localDateTime1.format(pattern));
        LocalDateTime localDateTime2 = localDateTime1.plusHours(i);
        log.info("时间4:{}", localDateTime2.format(pattern));
        LocalDateTime localDateTime3 = localDateTime2.plusHours(i);
        log.info("时间5:{}", localDateTime3.format(pattern));
        LocalDateTime localDateTime4 = localDateTime3.plusHours(i);
        log.info("时间6:{}", localDateTime4.format(pattern));
        LocalDateTime localDateTime5 = localDateTime4.plusHours(i);
        log.info("时间7:{}", localDateTime5.format(pattern));
        LocalDateTime localDateTime6 = localDateTime5.plusHours(i);
        log.info("时间8:{}", localDateTime6.format(pattern));
        LocalDateTime localDateTime7 = localDateTime6.plusHours(i);
        log.info("时间9:{}", localDateTime7.format(pattern));
        LocalDateTime localDateTime8 = localDateTime7.plusHours(i);
        log.info("时间10:{}", localDateTime8.format(pattern));
    }

    @Test
    public void test7() {
        int i = 13;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        log.info("时间1:{}", now.format(pattern));
        LocalDateTime localDateTime = now.plusMinutes(i * 60 + 20);
        log.info("时间2:{}", localDateTime.format(pattern));
        LocalDateTime localDateTime1 = localDateTime.plusMinutes(i * 60 + 20);
        log.info("时间3:{}", localDateTime1.format(pattern));
        LocalDateTime localDateTime2 = localDateTime1.plusMinutes(i * 60 + 20);
        log.info("时间4:{}", localDateTime2.format(pattern));
        LocalDateTime localDateTime3 = localDateTime2.plusMinutes(i * 60 + 20);
        log.info("时间5:{}", localDateTime3.format(pattern));
        LocalDateTime localDateTime4 = localDateTime3.plusMinutes(i * 60 + 20);
        log.info("时间6:{}", localDateTime4.format(pattern));
        LocalDateTime localDateTime5 = localDateTime4.plusMinutes(i * 60 + 20);
        log.info("时间7:{}", localDateTime5.format(pattern));
        LocalDateTime localDateTime6 = localDateTime5.plusMinutes(i * 60 + 20);
        log.info("时间8:{}", localDateTime6.format(pattern));
        LocalDateTime localDateTime7 = localDateTime6.plusMinutes(i * 60 + 20);
        log.info("时间9:{}", localDateTime7.format(pattern));
        LocalDateTime localDateTime8 = localDateTime7.plusMinutes(i * 60 + 20);
        log.info("时间10:{}", localDateTime8.format(pattern));
    }

    @Test
    public void test8() {
        int i = 13;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime nextTime = getNextTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        log.info("时间:{}", nextTime.format(pattern));
        LocalDateTime nextTime1 = getNextTime(nextTime.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime1.format(pattern));
        LocalDateTime nextTime2 = getNextTime(nextTime1.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime2.format(pattern));
        LocalDateTime nextTime3 = getNextTime(nextTime2.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime3.format(pattern));
        LocalDateTime nextTime4 = getNextTime(nextTime3.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime4.format(pattern));
        LocalDateTime nextTime5 = getNextTime(nextTime4.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime5.format(pattern));
        LocalDateTime nextTime6 = getNextTime(nextTime5.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime6.format(pattern));
        LocalDateTime nextTime7 = getNextTime(nextTime6.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime7.format(pattern));
        LocalDateTime nextTime8 = getNextTime(nextTime7.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime8.format(pattern));
        LocalDateTime nextTime9 = getNextTime(nextTime8.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime9.format(pattern));
        LocalDateTime nextTime10 = getNextTime(nextTime9.plusMinutes(i * 60 + 20));
        log.info("时间:{}", nextTime10.format(pattern));
    }

    @Test
    public void test9() {
        int i = new BigDecimal(0.5).multiply(new BigDecimal(60))
                .intValue();
        log.info("时间:{}", i);
    }

}
