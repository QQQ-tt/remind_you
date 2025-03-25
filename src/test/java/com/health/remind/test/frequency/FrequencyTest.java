package com.health.remind.test.frequency;

import com.health.remind.entity.RemindTask;
import com.health.remind.scheduler.ScheduledBase;
import com.health.remind.scheduler.enums.ScheduledEnum;
import com.health.remind.service.RemindTaskInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author QQQtx
 * @since 2025/3/25
 */
@SpringBootTest
@ActiveProfiles("dev")
public class FrequencyTest {

    @Autowired
    private RemindTaskInfoService remindTaskInfoService;

    @Test
    public void test() {
        remindTaskInfoService.putTask(RemindTask.builder().id(1904360768655552514L).isRemind(true).build());
        System.out.println(ScheduledBase.getFutureSize(ScheduledEnum.DELAY_SCHEDULED));
    }
}
