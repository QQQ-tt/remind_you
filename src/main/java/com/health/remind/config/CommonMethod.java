package com.health.remind.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.config.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QQQtx
 * @since 2024/11/26 11:10
 */
public class CommonMethod {

    // ------------------------请求转发---------------------------------

    @SneakyThrows
    public static void failed(HttpServletRequest request, HttpServletResponse response, DataEnums dataEnums) {
        request.setAttribute("jwtException", new AuthException(dataEnums));
        request.getRequestDispatcher("/exception/jwtException")
                .forward(request, response);
    }

    // ------------------------token放行校验---------------------------------

    private static final TrieNode ROOT = new TrieNode();

    public static boolean isPublicUrl(String url) {
        TrieNode node = ROOT;
        String[] parts = url.split("/");
        for (String part : parts) {
            if (node.children.containsKey(part)) {
                node = node.children.get(part);
            } else if (node.children.containsKey("*")) {
                node = node.children.get("*");
            } else if (node.children.containsKey("{var}")) {
                node = node.children.get("{var}");
            } else return node.children.containsKey("**");
        }
        return node.isEndOfWord;
    }

    public static void addPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            addPattern(pattern);
        }
    }

    private static void addPattern(String pattern) {
        TrieNode node = ROOT;
        String[] parts = pattern.split("/");
        for (String part : parts) {
            if (part.startsWith("{") && part.endsWith("}")) {
                part = "{var}";
            }
            node.children.putIfAbsent(part, new TrieNode());
            node = node.children.get(part);
        }
        node.isEndOfWord = true;
    }

    private static class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
    }

    // ------------------------用户基本信息---------------------------------

    /**
     * 用户信息
     */
    private static final ThreadLocal<Map<UserInfo, String>> mapThreadLocal = new ThreadLocal<>();

    public static String getUserName() {
        return mapThreadLocal.get()
                .get(UserInfo.USER_NAME);
    }

    public static Long getTenantId() {
        return Long.parseLong(mapThreadLocal.get()
                .get(UserInfo.tenant_id));
    }

    public static String getToken() {
        return mapThreadLocal.get()
                .get(UserInfo.TOKEN);
    }

    public static Long getUserId() {
        return Long.parseLong(mapThreadLocal.get()
                .get(UserInfo.USER_ID));
    }

    public static String getUrl() {
        return mapThreadLocal.get()
                .get(UserInfo.URL);
    }

    public static String getParameter() {
        return mapThreadLocal.get()
                .get(UserInfo.PARAMETER);
    }

    public static void setParameter(String parameter) {
        mapThreadLocal.get()
                .put(UserInfo.PARAMETER, parameter);
    }

    public static void setUrl(String url) {
        mapThreadLocal.get()
                .put(UserInfo.URL, url);
    }

    public static void setToken(String token) {
        mapThreadLocal.get()
                .put(UserInfo.TOKEN, token);
    }

    public static void setUserName(String userName) {
        if (StringUtils.isNotBlank(userName)) {
            mapThreadLocal.get()
                    .put(UserInfo.USER_NAME, userName);
        }
    }

    public static void setTenantId(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            mapThreadLocal.get()
                    .put(UserInfo.tenant_id, tenantId);
        }
    }

    public static void setUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            mapThreadLocal.get()
                    .put(UserInfo.USER_ID, userId);
        }
    }

    public static Map<UserInfo, String> getMap() {
        Map<UserInfo, String> map = mapThreadLocal.get();
        if (map == null) {
            initialize();
            return mapThreadLocal.get();
        }
        return map;
    }

    public static void setMap(Map<UserInfo, String> map) {
        mapThreadLocal.set(map);
    }

    public static void clear() {
        mapThreadLocal.remove();
    }

    public static void initialize() {
        Map<UserInfo, String> map = mapThreadLocal.get();
        if (map == null) {
            HashMap<UserInfo, String> value = new HashMap<>();
            value.put(UserInfo.USER_ID, "1");
            value.put(UserInfo.USER_NAME, "admin");
            value.put(UserInfo.tenant_id, "1234");
            mapThreadLocal.set(value);
        }
    }
}
