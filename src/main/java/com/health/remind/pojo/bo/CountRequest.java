package com.health.remind.pojo.bo;

import lombok.Data;

/**
 * 柱状图：count(url)
 * 找出最 热门 的 10 个接口
 *
 * @author QQQtx
 * @since 2025/3/11
 */
@Data
public class CountRequest {

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
}
