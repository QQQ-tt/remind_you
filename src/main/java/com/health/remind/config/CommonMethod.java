package com.health.remind.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.enums.UserInfo;
import com.health.remind.config.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
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

    /**
     * 判断是否为开放路径
     *
     * @param url 请求路径
     * @return 是否为开放路径
     */
    public static boolean isPublicUrl(String url) {
        return isPublicUrl(url, ROOT);
    }

    /**
     * 根据传入的TrieNode判断url是否为开放路径
     *
     * @param url  请求路径
     * @param node 路径节点
     * @return 是否符合路径
     */
    public static boolean isPublicUrl(String url, TrieNode node) {
        url = url.startsWith("/") ? url.substring(1) : url;
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
        patterns.forEach(CommonMethod::addPattern);
    }

    public static void addPatterns(List<String> patterns, TrieNode node) {
        patterns.forEach(pattern -> addPattern(pattern, node));
    }

    private static void addPattern(String pattern) {
        addPattern(pattern, ROOT);
    }

    public static void addPattern(String pattern, TrieNode node) {
        pattern = pattern.startsWith("/") ? pattern.substring(1) : pattern;
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

    @Data
    public static class TrieNode implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L; // 推荐添加序列化版本UID
        Map<String, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
    }

    // ------------------------用户基本信息---------------------------------

    /**
     * 用户信息
     */
    private static final ThreadLocal<Map<UserInfo, String>> THREAD_LOCAL = ThreadLocal.withInitial(
            CommonMethod::initialize);

    public static String getUserName() {
        return THREAD_LOCAL.get()
                .get(UserInfo.USER_NAME);
    }

    public static Long getTenantId() {
        return Long.parseLong(THREAD_LOCAL.get()
                .get(UserInfo.TENANT_ID));
    }

    public static String getToken() {
        return THREAD_LOCAL.get()
                .get(UserInfo.TOKEN);
    }

    public static Long getUserId() {
        return Long.parseLong(THREAD_LOCAL.get()
                .get(UserInfo.USER_ID));
    }

    public static String getUrl() {
        return THREAD_LOCAL.get()
                .get(UserInfo.URL);
    }

    public static String getIp() {
        return THREAD_LOCAL.get()
                .get(UserInfo.IP);
    }

    public static String getParameter() {
        return THREAD_LOCAL.get()
                .get(UserInfo.PARAMETER);
    }

    public static String getMethod() {
        return THREAD_LOCAL.get()
                .get(UserInfo.METHOD);
    }

    public static void setMethod(String method) {
        THREAD_LOCAL.get()
                .put(UserInfo.METHOD, method);
    }

    public static void setParameter(String parameter) {
        THREAD_LOCAL.get()
                .put(UserInfo.PARAMETER, parameter);
    }

    public static void setUrl(String url) {
        THREAD_LOCAL.get()
                .put(UserInfo.URL, url);
    }

    public static void setIp(String ip) {
        THREAD_LOCAL.get()
                .put(UserInfo.IP, ip);
    }

    public static void setToken(String token) {
        THREAD_LOCAL.get()
                .put(UserInfo.TOKEN, token);
    }

    public static void setUserName(String userName) {
        if (StringUtils.isNotBlank(userName)) {
            THREAD_LOCAL.get()
                    .put(UserInfo.USER_NAME, userName);
        }
    }

    public static void setTenantId(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            THREAD_LOCAL.get()
                    .put(UserInfo.TENANT_ID, tenantId);
        }
    }

    public static void setUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            THREAD_LOCAL.get()
                    .put(UserInfo.USER_ID, userId);
        }
    }

    public static Map<UserInfo, String> getMap() {
        return Collections.unmodifiableMap(THREAD_LOCAL.get());
    }

    public static void setMap(Map<UserInfo, String> map) {
        THREAD_LOCAL.set(map);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public static HashMap<UserInfo, String> initialize() {
        HashMap<UserInfo, String> value = new HashMap<>();
        value.put(UserInfo.USER_ID, "1");
        value.put(UserInfo.USER_NAME, "admin");
        value.put(UserInfo.TENANT_ID, "1234");
        return value;
    }
}
