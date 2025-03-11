package com.health.remind.pojo.bo;

import lombok.Data;

/**
 * IP 分布图
 * 识别 爬虫、攻击流量
 *
 * @author QQQtx
 * @since 2025/3/11
 */
@Data
public class IpCountRequest {

    /**
     * ip地址
     */
    private String ip;

    /**
     * 请求次数
     */
    private int requestCount;
}
