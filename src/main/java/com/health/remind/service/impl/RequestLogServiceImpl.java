package com.health.remind.service.impl;

import com.health.remind.entity.RequestLog;
import com.health.remind.mapper.RequestLogMapper;
import com.health.remind.pojo.bo.CountRequest;
import com.health.remind.pojo.bo.ErrorCountRequest;
import com.health.remind.pojo.bo.HighConcurrencyRequest;
import com.health.remind.pojo.bo.IpCountRequest;
import com.health.remind.pojo.bo.SlowRequest;
import com.health.remind.service.RequestLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 请求日志 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-03-11
 */
@Service
public class RequestLogServiceImpl extends ServiceImpl<RequestLogMapper, RequestLog> implements RequestLogService {

    @Override
    public List<SlowRequest> listSlowRequest() {
        return baseMapper.selectSlowRequest();
    }

    @Override
    public List<CountRequest> listCountRequest() {
        return baseMapper.selectCountRequest();
    }

    @Override
    public List<HighConcurrencyRequest> listHighConcurrencyRequest() {
        return baseMapper.selectHighConcurrencyRequest();
    }

    @Override
    public List<ErrorCountRequest> listErrorCountRequest() {
        return baseMapper.selectErrorCountRequest();
    }

    @Override
    public List<IpCountRequest> listIpCountRequest() {
        return baseMapper.selectIpCountRequest();
    }
}
