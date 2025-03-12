package com.health.remind.controller;

import com.health.remind.config.R;
import com.health.remind.pojo.bo.CountRequest;
import com.health.remind.pojo.bo.ErrorCountRequest;
import com.health.remind.pojo.bo.HighConcurrencyRequest;
import com.health.remind.pojo.bo.IpCountRequest;
import com.health.remind.pojo.bo.SlowRequest;
import com.health.remind.service.RequestLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 请求日志 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-03-11
 */
@Tag(name = "请求日志")
@RestController
@RequestMapping("/remind/requestLog")
public class RequestLogController {

    private final RequestLogService requestLogService;

    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @Operation(summary = "慢请求")
    @GetMapping("/listSlowRequest")
    public R<List<SlowRequest>> listSlowRequest() {
        return R.success(requestLogService.listSlowRequest());
    }

    @Operation(summary = "请求次数")
    @GetMapping("/listCountRequest")
    public R<List<CountRequest>> listCountRequest() {
        return R.success(requestLogService.listCountRequest());
    }

    @Operation(summary = "高并发请求")
    @GetMapping("/listHighConcurrencyRequest")
    public R<List<HighConcurrencyRequest>> listHighConcurrencyRequest() {
        return R.success(requestLogService.listHighConcurrencyRequest());
    }

    @Operation(summary = "错误请求次数")
    @GetMapping("/listErrorCountRequest")
    public R<List<ErrorCountRequest>> listErrorCountRequest() {
        return R.success(requestLogService.listErrorCountRequest());
    }

    @Operation(summary = "ip请求次数")
    @GetMapping("/listIpCountRequest")
    public R<List<IpCountRequest>> listIpCountRequest() {
        return R.success(requestLogService.listIpCountRequest());
    }
}
