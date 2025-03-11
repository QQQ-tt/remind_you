package com.health.remind.scheduler;

import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.ExceptionLog;
import com.health.remind.entity.RequestLog;
import com.health.remind.scheduler.entity.ExceptionTask;
import com.health.remind.service.ExceptionLogService;
import com.health.remind.service.RequestLogService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author QQQtx
 * @since 2025/2/8
 */
@Component
public class LogConsumerExecutor {

    private final ExceptionLogService exceptionLogService;

    private final RequestLogService requestLogService;

    private final ThreadPoolExecutor threadPoolExecutor;

    public LogConsumerExecutor(ExceptionLogService exceptionLogService, RequestLogService requestLogService, ThreadPoolExecutor threadPoolExecutor) {
        this.exceptionLogService = exceptionLogService;
        this.requestLogService = requestLogService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void putExceptionTask(ExceptionTask exceptionTask) {
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

    public void putLogTask(RequestLog requestLog) {
        Map<UserInfo, String> map = CommonMethod.getMap();
        threadPoolExecutor.submit(() -> {
            CommonMethod.setMap(map);
            requestLogService.save(requestLog);
        });
    }
}
