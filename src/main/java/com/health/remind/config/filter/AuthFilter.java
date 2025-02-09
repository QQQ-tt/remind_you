package com.health.remind.config.filter;

import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.UserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author QQQtx
 * @since 2024/11/26 12:45
 */
@Slf4j
@WebFilter("/*")
public class AuthFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CommonMethod.initialize();
        CommonMethod.setUserId(request.getHeader(UserInfo.USER_ID.toString()));
        String userName = request.getHeader(UserInfo.USER_NAME.toString());
        if (userName != null && userName.contains("%")) {
            userName = URLDecoder.decode(userName, StandardCharsets.UTF_8);
        }
        CommonMethod.setUserName(userName);
        CommonMethod.setTenantId(request.getHeader(UserInfo.tenant_id.toString()));
        if (!request.getRequestURI()
                .equals("/")) {
            String requestURI = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();
            CommonMethod.setUrl(remoteAddr + requestURI);
            CommonMethod.setParameter(request.getQueryString());
            log.info("用户信息:{}", CommonMethod.getMap());
        }
        filterChain.doFilter(request, response);
        CommonMethod.clear();
    }
}
