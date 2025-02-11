package com.health.remind.scheduler.entity;

import com.health.remind.config.enums.UserInfo;
import com.health.remind.scheduler.enums.ExecutionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author QQQtx
 * @since 2025/1/6 16:43
 */
@Data
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
    private final ExecutionEnum executionEnum;

    /**
     * 用户信息
     */
    private final Map<UserInfo, String> commonMethod;

    /**
     * 单次执行时间
     */
    private final LocalDateTime executeTime;

    /**
     * 上次执行时间
     */
    @Setter
    private LocalDateTime lastExecutionTime = LocalDateTime.now();

    public DelayTask(Long id, ExecutionEnum executionEnum, Map<UserInfo, String> commonMethod) {
        this.id = id;
        this.executionEnum = executionEnum;
        this.commonMethod = commonMethod;
        this.executeTime = LocalDateTime.now();
        this.outTradeNo = "";
    }

    public DelayTask(Long id, LocalDateTime executeTime,
                     ExecutionEnum executionEnum, Map<UserInfo, String> commonMethod) {
        this.id = id;
        this.executionEnum = executionEnum;
        this.commonMethod = commonMethod;
        this.executeTime = executeTime;
        this.outTradeNo = "";
    }
}
