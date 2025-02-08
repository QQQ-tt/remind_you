package com.health.remind.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.health.remind.config.enums.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QQQtx
 * @since 2024/11/26 11:10
 */
public class CommonMethod {

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
        return mapThreadLocal.get();
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
