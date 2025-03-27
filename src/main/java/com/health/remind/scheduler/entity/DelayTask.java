package com.health.remind.scheduler.entity;

import com.health.remind.config.enums.UserInfo;
import com.health.remind.scheduler.enums.ExecutionEnum;
import com.health.remind.scheduler.enums.RemindTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author QQQtx
 * @since 2025/1/6 16:43
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DelayTask {

    /**
     * 任务id
     */
    private final Long id;

    /**
     * 商户订单号
     */
    private final String outTradeNo;

    /**
     * 执行次数
     */
    private final AtomicInteger attemptCount = new AtomicInteger(1);

    /**
     * 查询类型
     */
    private ExecutionEnum executionEnum;
    private RemindTypeEnum remindTypeEnum;

    /**
     * 用户信息
     */
    private final Map<UserInfo, String> commonMethod;

    /**
     * 单次执行时间
     */
    private final LocalDateTime executeTime;

    /**
     * 其他id
     */
    private final String otherId;

    private final Map<String, String> otherMap;

    /**
     * 上次执行时间
     */
    @Setter
    private LocalDateTime lastExecutionTime = LocalDateTime.now();

    public DelayTask(Long id, ExecutionEnum executionEnum, Map<UserInfo, String> commonMethod) {
        this.id = id;
        this.executionEnum = executionEnum;
        this.commonMethod = commonMethod;
        this.otherId = "";
        this.executeTime = LocalDateTime.now();
        this.outTradeNo = "";
        this.otherMap = null;
    }

    public DelayTask(Long id, LocalDateTime executeTime,
                     RemindTypeEnum remindTypeEnum, Map<UserInfo, String> commonMethod, String otherId, Map<String, String> otherMap) {
        this.id = id;
        this.remindTypeEnum = remindTypeEnum;
        this.commonMethod = commonMethod;
        this.executeTime = executeTime;
        this.otherId = otherId;
        this.outTradeNo = "";
        this.otherMap = otherMap;
    }
}
