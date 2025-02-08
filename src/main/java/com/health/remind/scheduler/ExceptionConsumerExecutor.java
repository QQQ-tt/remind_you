package com.health.remind.scheduler;

import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.ExceptionLog;
import com.health.remind.scheduler.entity.ExceptionTask;
import com.health.remind.service.ExceptionLogService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author QQQtx
 * @since 2025/2/8
 */
@Component
public class ExceptionConsumerExecutor {

    private final ExceptionLogService exceptionLogService;

    private final ThreadPoolExecutor threadPoolExecutor;

    public ExceptionConsumerExecutor(ExceptionLogService exceptionLogService, ThreadPoolExecutor threadPoolExecutor, ThreadPoolExecutor threadPoolExecutor1) {
        this.exceptionLogService = exceptionLogService;
        this.threadPoolExecutor = threadPoolExecutor1;
    }

    public void putTask(ExceptionTask exceptionTask) {
        Map<UserInfo, String> map = CommonMethod.getMap();
        threadPoolExecutor.submit(() -> {
            CommonMethod.setMap(map);
            exceptionLogService.save(ExceptionLog.builder()
                    .exceptionName(exceptionTask.getExceptionName())
                    .url(map.get(UserInfo.URL))
                    .parameter(map.get(UserInfo.PARAMETER))
                    .level(exceptionTask.getLevel())
                    .message(exceptionTask.getMessage())
                    .stackTrace(exceptionTask.getStackTrace())
                    .build());
        });
    }
}
