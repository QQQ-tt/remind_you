package com.health.remind.config.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.entity.RequestLog;
import com.health.remind.scheduler.LogConsumerExecutor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QQQtx
 * @since 2025/3/11
 */
@Slf4j
@Order(1)
@WebFilter("/*")
public class CommonFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final LogConsumerExecutor requestLogConsumerExecutor;

    public CommonFilter(ObjectMapper objectMapper, LogConsumerExecutor requestLogConsumerExecutor) {
        this.objectMapper = objectMapper;
        this.requestLogConsumerExecutor = requestLogConsumerExecutor;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 开始时间
        long startTime = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();
        CommonMethod.setMethod(request.getMethod());
        CommonMethod.setTenantId(request.getHeader(UserInfo.TENANT_ID.toString()));
        String userAgent = request.getHeader("User-Agent");

        String headers = getHeadersAsJson(request);
        if (!request.getRequestURI()
                .equals("/")) {
            String requestURI = request.getRequestURI();
            String remoteAddr;
            String header = request.getHeader("X-Real-IP");
            if (StringUtils.isBlank(header)) {
                remoteAddr = request.getRemoteAddr();
            } else {
                remoteAddr = header;
            }
            CommonMethod.setIp(remoteAddr);
            CommonMethod.setUrl(remoteAddr + requestURI);
            log.info("用户信息:{}", CommonMethod.getMap());
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        String params = "";
        if (!parameterMap.isEmpty()) {
            params = objectMapper.writeValueAsString(parameterMap);
        }
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(cachedRequest, response);

        if (StringUtils.isBlank(params)) {
            params = new String(cachedRequest.getContentAsByteArray(), StandardCharsets.UTF_8).trim();
        }
        CommonMethod.setParameter(params);
        // 计算请求时间
        long duration = System.currentTimeMillis() - startTime;
        int statusCode = response.getStatus();
        int contentLength = request.getContentLength();
        RequestLog log = new RequestLog();
        log.setIp(CommonMethod.getIp());
        log.setUrl(CommonMethod.getUrl());
        log.setMethod(CommonMethod.getMethod());
        log.setTime(now);
        log.setSize((int) duration);
        log.setStatusCode(String.valueOf(statusCode));
        log.setUserAgent(userAgent);
        log.setParams(params);
        log.setHeaders(headers);
        log.setResponseLength(contentLength);
        requestLogConsumerExecutor.putLogTask(log);
        CommonMethod.clear();
    }

    private String getHeadersAsJson(HttpServletRequest request) throws JsonProcessingException {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }
        return new ObjectMapper().writeValueAsString(headersMap);
    }
}
