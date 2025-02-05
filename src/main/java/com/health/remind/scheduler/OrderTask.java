package com.health.remind.scheduler;

import com.health.remind.config.enums.UserInfo;
import com.health.remind.wx.enums.QueryEnum;
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
public class OrderTask {

    private final Long id;

    private final String outTradeNo;

    private final AtomicInteger attemptCount;

    @Setter
    private LocalDateTime lastExecutionTime;

    private LocalDateTime endTime;

    private final QueryEnum queryEnum;

    private final Map<UserInfo, String> commonMethod;

    public OrderTask(Long id, String outTradeNo, QueryEnum queryEnum, Map<UserInfo, String> commonMethod) {
        this.id = id;
        this.outTradeNo = outTradeNo;
        this.attemptCount = new AtomicInteger(0);
        this.lastExecutionTime = LocalDateTime.now();
        this.queryEnum = queryEnum;
        this.commonMethod = commonMethod;
    }

    public OrderTask(Long id, QueryEnum queryEnum, Map<UserInfo, String> commonMethod) {
        this.id = id;
        this.attemptCount = new AtomicInteger(1);
        this.lastExecutionTime = LocalDateTime.now();
        this.queryEnum = queryEnum;
        this.commonMethod = commonMethod;
        outTradeNo = "";
    }
}
