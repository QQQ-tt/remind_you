package com.health.remind.pojo.bo;

import lombok.Data;

/**
 * 饼图（HTTP 状态码占比）
 * 找出 异常请求最多 的接口。
 * 计算 接口错误率。
 *
 * @author QQQtx
 * @since 2025/3/11
 */
@Data
public class ErrorCountRequest {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 错误数量
     */
    private int errorCount;

    /**
     * 错误率
     */
    private String errorRate;
}
