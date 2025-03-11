package com.health.remind.pojo.bo;

import lombok.Data;

/**
 * 折线图：avg(size)
 * 找到 平均请求时间最长的前 10 个接口
 *
 * @author QQQtx
 * @since 2025/3/11
 */
@Data
public class SlowRequest {

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
     * 平均值时间
     */
    private int avgTime;

    /**
     * 最大时间
     */
    private int maxTime;
}
