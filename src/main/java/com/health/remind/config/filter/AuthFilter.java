package com.health.remind.config.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.util.JwtUtils;
import com.health.remind.util.RedisUtils;
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
        CommonMethod.setTenantId(request.getHeader(UserInfo.tenant_id.toString()));
        if (!request.getRequestURI()
                .equals("/")) {
            String requestURI = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();
            CommonMethod.setUrl(remoteAddr + requestURI);
            CommonMethod.setParameter(request.getQueryString());
            log.info("用户信息:{}", CommonMethod.getMap());
        }
        if (CommonMethod.isPublicUrl(request.getRequestURI().startsWith("/") ? request.getRequestURI().substring(1) :
                request.getRequestURI())) {
            filterChain.doFilter(request, response);
            CommonMethod.clear();
            return;
        }
        String auth = request.getHeader(UserInfo.TOKEN.toString());
        if (StringUtils.isBlank(auth)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return;
        }
        String token = auth.split(" ")[1];
        Boolean tokenExpired = JwtUtils.isTokenExpired(token);
        if (tokenExpired) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return;
        }
        String bodyFromToken = JwtUtils.getBodyFromToken(token);
        if (StringUtils.isBlank(bodyFromToken)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return;
        }
        LoginVO loginVO = RedisUtils.getObject(RedisKeys.getLoginKey(bodyFromToken), LoginVO.class);
        if (loginVO == null || !loginVO.getToken()
                .equals(token)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return;
        }
        CommonMethod.setUserId(bodyFromToken);
        String userName = request.getHeader(UserInfo.USER_NAME.toString());
        if (userName != null && userName.contains("%")) {
            userName = URLDecoder.decode(userName, StandardCharsets.UTF_8);
        }
        if (StringUtils.isBlank(userName)) {
            userName = loginVO.getName();
        }
        CommonMethod.setUserName(userName);
        CommonMethod.setToken(token);
        filterChain.doFilter(request, response);
        CommonMethod.clear();
    }
}
