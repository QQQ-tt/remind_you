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
        log.debug("start insert fill ....");
        // strictInsertFill 是为null时更新
        this.strictInsertFill(metaObject, "createId", Long.class, CommonMethod.getAccount());
        this.strictInsertFill(metaObject, "createName", String.class, CommonMethod.getUserName());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "tenantId", Long.class, CommonMethod.getTenantId());
        log.debug("end insert fill ...");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        // setFieldValByName 强制更新对应字段
        this.setFieldValByName("updateId", CommonMethod.getAccount(), metaObject);
        this.setFieldValByName("updateName", CommonMethod.getUserName(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        log.debug("end update fill ...");
    }
}

