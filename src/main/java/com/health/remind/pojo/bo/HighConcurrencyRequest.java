package com.health.remind.pojo.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 柱状图：count(url)
 * 找到瞬时高并发接口
 *
 * @author QQQtx
 * @since 2025/3/11
 */
@Data
public class HighConcurrencyRequest {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求次数
     */
    private int requestCount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;
}
