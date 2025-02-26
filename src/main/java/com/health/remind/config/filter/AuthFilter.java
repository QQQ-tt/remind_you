package com.health.remind.config.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.cache.JavaCache;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.util.JwtUtils;
import com.health.remind.util.RedisUtils;
import io.jsonwebtoken.Claims;
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

    private final JavaCache cache;

    public AuthFilter(JavaCache cache) {
        this.cache = cache;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CommonMethod.setTenantId(request.getHeader(UserInfo.TENANT_ID.toString()));
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
            CommonMethod.setUrl(remoteAddr + requestURI);
            CommonMethod.setParameter(request.getQueryString());
            log.info("用户信息:{}", CommonMethod.getMap());
        }

        if (CommonMethod.isPublicUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            CommonMethod.clear();
            return;
        }

        Token result = getToken(request, response);
        if (result == null) return;

        if (resource(request, response, result.claims())) return;

        CommonMethod.setUserId(result.bodyFromToken());
        String userName = request.getHeader(UserInfo.USER_NAME.toString());
        if (userName != null && userName.contains("%")) {
            userName = URLDecoder.decode(userName, StandardCharsets.UTF_8);
        }
        if (StringUtils.isBlank(userName)) {
            userName = result.loginVO()
                    .getName();
        }
        CommonMethod.setUserName(userName);
        CommonMethod.setToken(result.token());
        filterChain.doFilter(request, response);
        CommonMethod.clear();
    }

    private static Token getToken(HttpServletRequest request, HttpServletResponse response) {
        String auth = request.getHeader(UserInfo.TOKEN.toString());
        if (StringUtils.isBlank(auth)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return null;
        }
        String token = auth.split(" ")[1];
        Boolean tokenExpired = JwtUtils.isTokenExpired(token);
        if (tokenExpired) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return null;
        }

        String bodyFromToken = JwtUtils.getBodyFromToken(token);
        Claims claims = JwtUtils.getClaimsFromToken(token);
        if (StringUtils.isBlank(bodyFromToken)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return null;
        }

        String type = claims.get(StaticConstant.USER_TYPE, String.class);
        LoginVO loginVO = RedisUtils.getObject(RedisKeys.getLoginKey(bodyFromToken, type), LoginVO.class);
        if (loginVO == null || !loginVO.getToken()
                .equals(token)) {
            CommonMethod.failed(request, response, DataEnums.USER_NOT_LOGIN);
            return null;
        }
        return new Token(token, bodyFromToken, claims, loginVO);
    }

    private record Token(String token, String bodyFromToken, Claims claims, LoginVO loginVO) {
    }

    /**
     * 用户资源校验
     */
    private boolean resource(HttpServletRequest request, HttpServletResponse response, Claims claims) {
        Long roleId = claims.get(StaticConstant.ROLE_ID, Long.class);
        if (roleId == null) {
            CommonMethod.failed(request, response, DataEnums.USER_ROLE_ERROR);
            return true;
        }
        CommonMethod.TrieNode verify = cache.verify(roleId);
        boolean publicUrl = CommonMethod.isPublicUrl(request.getRequestURI(), verify);
        if (!publicUrl) {
            CommonMethod.failed(request, response, DataEnums.USER_RESOURCE_ERROR);
            return true;
        }
        return false;
    }
}
