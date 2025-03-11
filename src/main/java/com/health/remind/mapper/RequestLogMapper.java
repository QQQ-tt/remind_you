package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.remind.entity.RequestLog;
import com.health.remind.pojo.bo.CountRequest;
import com.health.remind.pojo.bo.ErrorCountRequest;
import com.health.remind.pojo.bo.HighConcurrencyRequest;
import com.health.remind.pojo.bo.IpCountRequest;
import com.health.remind.pojo.bo.SlowRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 请求日志 Mapper 接口
 * </p>
 *
 * @author QQQtx
 * @since 2025-03-11
 */
@Mapper
public interface RequestLogMapper extends BaseMapper<RequestLog> {

    List<SlowRequest> selectSlowRequest();

    List<CountRequest> selectCountRequest();

    List<HighConcurrencyRequest> selectHighConcurrencyRequest();

    List<ErrorCountRequest> selectErrorCountRequest();

    List<IpCountRequest> selectIpCountRequest();
}
