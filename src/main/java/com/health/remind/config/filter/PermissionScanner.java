package com.health.remind.config.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.util.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 扫描接口是否需要token
 *
 * @author QQQtx
 * @since 2025/2/18
 */
@Component
public class PermissionScanner {

    private final ApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    public PermissionScanner(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void generatePermissionConfig() {
        // 获取springboot的名字
        String applicationName = applicationContext.getId();
        Set<String> keys = RedisUtils.keys(RedisKeys.getTokenPermissionKey(applicationName, null));
        RedisUtils.delete(keys);
        List<Map<String, String>> permissions = new ArrayList<>();
        List<String> publicPath = new ArrayList<>();

        // 获取所有 包含 @Tag 注解的类
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(RestController.class);
        Arrays.stream(beanNames)
                .forEach(beanName -> {
                    Object controllerBean = applicationContext.getBean(beanName);
                    Class<?> controllerClass = controllerBean.getClass();

                    Tag tagAnnotation = controllerClass.getAnnotation(Tag.class);
                    // 模块名称
                    String moduleName = (tagAnnotation != null) ? tagAnnotation.name() : "Default Module";
                    // 获取路径和方法信息
                    RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
                    // 路径
                    String parentPath = (requestMapping != null && requestMapping.value().length > 0) ?
                            requestMapping.value()[0].startsWith("/") ? requestMapping.value()[0]
                                    .substring(1) : requestMapping.value()[0] : "";
                    for (Method method : controllerClass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Operation.class)) {
                            String childPath = parentPath;
                            // 获取 @Operation 信息
                            Operation operation = method.getAnnotation(Operation.class);
                            // 接口描述
                            String summary = operation.summary();
                            // 权限描述
                            String description = operation.description();
                            // HTTP 方法
                            String httpMethod = "GET";
                            if (method.isAnnotationPresent(PostMapping.class)) {
                                childPath += method.getAnnotation(PostMapping.class)
                                        .value()[0];
                                httpMethod = "POST";
                            } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                                childPath += method.getAnnotation(DeleteMapping.class)
                                        .value()[0];
                                httpMethod = "DELETE";
                            } else if (method.isAnnotationPresent(GetMapping.class)) {
                                childPath += method.getAnnotation(GetMapping.class)
                                        .value()[0];
                                httpMethod = "GET";
                            } else if (method.isAnnotationPresent(PutMapping.class)) {
                                childPath += method.getAnnotation(PutMapping.class)
                                        .value()[0];
                                httpMethod = "GET";
                            }
                            // 构建权限配置
                            Map<String, String> objectMap = Map.of(
                                    "moduleName", moduleName,
                                    "path", childPath,
                                    "httpMethod", httpMethod,
                                    "summary", summary,
                                    "public", description);
                            String s;
                            try {
                                s = objectMapper.writeValueAsString(objectMap);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            if (StaticConstant.PERMISSION_KEY.equals(description)) {
                                publicPath.add(childPath);
                                RedisUtils.hPut(
                                        RedisKeys.getTokenPermissionKey(applicationName, StaticConstant.PERMISSION_KEY),
                                        childPath, s);
                            }
                            permissions.add(objectMap);
                            RedisUtils.hPut(RedisKeys.getTokenPermissionKey(applicationName, moduleName),
                                    childPath, s);
                        }
                    }
                });
        CommonMethod.addPatterns(publicPath);
    }
}
