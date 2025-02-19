package com.health.remind.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Component
public class ScheduledTasks {

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredData() {
        // 定时删除过期数据
    }
}
