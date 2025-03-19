package com.health.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.remind.entity.RequestLog;
import com.health.remind.pojo.bo.CountRequest;
import com.health.remind.pojo.bo.ErrorCountRequest;
import com.health.remind.pojo.bo.HighConcurrencyRequest;
import com.health.remind.pojo.bo.IpCountRequest;
import com.health.remind.pojo.bo.SlowRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    List<SlowRequest> selectSlowRequest(@Param("dayNum") int num);

    List<CountRequest> selectCountRequest(@Param("dayNum") int num);

    List<HighConcurrencyRequest> selectHighConcurrencyRequest(@Param("dayNum") int num, @Param("time") int time);

    List<ErrorCountRequest> selectErrorCountRequest(@Param("dayNum") int num);

    List<IpCountRequest> selectIpCountRequest(@Param("dayNum") int num);
}
