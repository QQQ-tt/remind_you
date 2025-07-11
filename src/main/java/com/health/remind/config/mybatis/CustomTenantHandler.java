package com.health.remind.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.health.remind.config.CommonMethod;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Component
public class CustomTenantHandler implements TenantLineHandler {

    private static final Set<String> igTable = new HashSet<>();

    static {
        igTable.add("request_log");
    }

    @Override
    public Expression getTenantId() {
        // 假设有一个租户上下文，能够从中获取当前用户的租户
        Long tenantId = CommonMethod.getTenantId();
        // 返回租户ID的表达式，LongValue 是 JSQLParser 中表示 bigint 类型的 class
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 根据需要返回是否忽略该表
        return igTable.contains(tableName);
    }

}