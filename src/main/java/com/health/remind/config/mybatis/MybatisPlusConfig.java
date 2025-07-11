package com.health.remind.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QQQtx
 * @since 2022/10/30 17:11
 */
@Configuration
public class MybatisPlusConfig {

    private final CustomTenantHandler customTenantHandler;

    public MybatisPlusConfig(CustomTenantHandler customTenantHandler) {
        this.customTenantHandler = customTenantHandler;
    }

    /**
     * 注册插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptorM() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(customTenantHandler);
        interceptor.addInnerInterceptor(tenantInterceptor);

        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }

}
