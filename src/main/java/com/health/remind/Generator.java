package com.health.remind;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.health.remind.config.BaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 代码生成器
 *
 * @author QQQtx
 * @since 2023/8/31 09:03
 */
public class Generator {

    private static final String URL;

    private static final String AUTHOR;

    private static final String PASSWORD;

    private static final String dataName;

    static {
        Map<String, String> getEnv = System.getenv();
        AUTHOR = Optional.ofNullable(getEnv.get("java_author"))
                .orElseThrow(() -> new RuntimeException("请设置java_author环境变量"));
        URL = StringUtils.isBlank(getEnv.get("service_ip")) ? "127.0.0.1" : getEnv.get("service_ip");
        PASSWORD = StringUtils.isBlank(getEnv.get("mysql_password")) ? "123456" : getEnv.get("mysql_password");
        dataName = Optional.ofNullable(getEnv.get("data_name"))
                .orElseThrow(() -> new RuntimeException("请设置data_name环境变量"));
    }

    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(
            "jdbc:mysql://" + URL + ":3306/" + dataName,
            "root",
            PASSWORD);

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> builder.author(AUTHOR)
                        .enableSpringdoc()
                        .outputDir(projectPath + "/src/main/java")
                        .disableOpenDir())
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent("com.health")
                        .moduleName("remind")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath +
                                "/src/main/resources/mapper")))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply(
                                "请输入表名，多个英文逗号分隔？所有输入 all")))
                        .controllerBuilder()
                        .enableRestStyle()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .entityBuilder()
                        .disableSerialVersionUID()
                        .enableLombok()
                        .superClass(BaseEntity.class)
                        .enableTableFieldAnnotation()
                        .addSuperEntityColumns("id",
                                "delete_flag",
                                "tenant_id",
                                "create_id",
                                "create_name",
                                "create_time",
                                "update_id",
                                "update_name",
                                "update_time")
                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class)
                        .build())
                .execute();
    }

    /**
     * 处理 all 情况
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
