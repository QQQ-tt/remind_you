package com.health.remind.config.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author QQQtx
 * @since 2025/2/7 15:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    // 锁前缀
    String lockPrefix() default "";

    // 方法参数名（用于取参数名的值与锁前缀拼接成锁名），尽量不要用对象map等，对象会toString后与锁前缀拼接
    String lockParameter() default "";

    // 尝试加锁，最多等待时间（毫秒）
    long lockWait() default 50L;

    // 自动解锁时间 （毫秒）
    long autoUnlockTime() default 2000L;

    // 重试次数
    int retryNum() default 3;

    // 重试等待时间 （毫秒）
    long retryWait() default 50L;
}
