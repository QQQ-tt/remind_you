package com.health.remind.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.health.remind.config.CommonMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author QQQtx
 * @since 2022/8/30
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createId", Long.class, CommonMethod.getUserId());
        this.strictInsertFill(metaObject, "createName", String.class, CommonMethod.getUserName());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "tenantId", Long.class, CommonMethod.getTenantId());
        log.info("end insert fill ...");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateId", Long.class, CommonMethod.getUserId());
        this.strictUpdateFill(metaObject, "updateName", String.class, CommonMethod.getUserName());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        log.info("end update fill ...");
    }
}

