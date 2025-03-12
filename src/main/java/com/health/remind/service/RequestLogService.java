package com.health.remind.service;

import com.health.remind.entity.RequestLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.pojo.bo.CountRequest;
import com.health.remind.pojo.bo.ErrorCountRequest;
import com.health.remind.pojo.bo.HighConcurrencyRequest;
import com.health.remind.pojo.bo.IpCountRequest;
import com.health.remind.pojo.bo.SlowRequest;

import java.util.List;

/**
 * <p>
 * 请求日志 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-03-11
 */
public interface RequestLogService extends IService<RequestLog> {

    /**
     * 折线图：avg(size)
     * 找到 平均请求时间最长的前 10 个接口
     */
    List<SlowRequest> listSlowRequest();

    /**
     * 柱状图：count(url)
     * 找出最 热门 的 10 个接口
     */
    List<CountRequest> listCountRequest();

    /**
     * 柱状图：count(url)
     * 找到瞬时高并发接口
     */
    List<HighConcurrencyRequest> listHighConcurrencyRequest();

    /**
     * 饼图（HTTP 状态码占比）
     * 找出 异常请求最多 的接口。
     * 计算 接口错误率。
     */
    List<ErrorCountRequest> listErrorCountRequest();

    /**
     * IP 分布图
     * 识别 爬虫、攻击流量
     */
    List<IpCountRequest> listIpCountRequest();

}
