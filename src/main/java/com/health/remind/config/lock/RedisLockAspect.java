package com.health.remind.config.lock;

import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author QQQtx
 * @since 2025/2/7 17:38
 */
@Aspect
@Slf4j
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    public RedisLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(redisLock)")
    private void myLock(RedisLock redisLock) {
    }

    @Around(value = "myLock(redisLock)", argNames = "joinPoint,redisLock")
    public Object redisLock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        // 获取注解参数
        Map<String, Object> annotationArgs = this.getAnnotationArgs(joinPoint, redisLock);
        String lockName = (String) annotationArgs.get(RedisLockConstants.LOCK_NAME);
        Assert.notNull(lockName, "锁名不能为空");

        int retryNum = (int) annotationArgs.get(RedisLockConstants.RETRY_NUM);
        long retryWait = (long) annotationArgs.getOrDefault(RedisLockConstants.RETRY_WAIT, 200L);  // 默认重试时间为200ms
        long lockWait = (long) annotationArgs.get(RedisLockConstants.LOCK_WAIT);
        long autoUnlockTime = (long) annotationArgs.get(RedisLockConstants.AUTO_UNLOCK_TIME);

        // 获取锁
        RLock lock = redissonClient.getLock(lockName);
        try {
            if (lock.tryLock(lockWait, autoUnlockTime, TimeUnit.SECONDS)) {
                log.info("{}: 加锁成功", lockName);
                return joinPoint.proceed();
            } else {
                return tryLockWithRetry(joinPoint, lock, lockName, retryNum, retryWait, lockWait, autoUnlockTime);
            }
        } catch (Throwable throwable) {
            log.error("分布式锁发生异常, 锁名: {}, 异常: {}", lockName, throwable.getMessage(), throwable);
            throw throwable;
        } finally {
            lock.unlock();
            log.info("{}: 释放锁成功", lockName);
        }
    }

    private Object tryLockWithRetry(ProceedingJoinPoint joinPoint, RLock lock, String lockName, int retryNum, long retryWait, long lockWait, long autoUnlockTime) throws Throwable {
        if (retryNum <= 0) {
            log.info("{} 已被锁定, 无重试", lockName);
            throw new DataException(DataEnums.SYSTEM_BUSY);
        }

        int failCount = 1;
        while (failCount <= retryNum) {
            Thread.sleep(retryWait);
            if (lock.tryLock(lockWait, autoUnlockTime, TimeUnit.SECONDS)) {
                log.info("{}: 重试加锁成功", lockName);
                return joinPoint.proceed();
            } else {
                log.info("{}: 重试中 [{}/{}], 等待时间 {}ms", lockName, failCount, retryNum, retryWait);
                failCount++;
            }
        }
        throw new DataException(DataEnums.SYSTEM_BUSY);
    }

    private Map<String, Object> getAnnotationArgs(ProceedingJoinPoint proceeding, RedisLock redisLock) {
        MethodSignature methodSignature = (MethodSignature) proceeding.getSignature();
        List<String> paramNames = Arrays.asList(methodSignature.getParameterNames());
        List<Object> paramValues = Arrays.asList(proceeding.getArgs());

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.size(); i++) {
            context.setVariable(paramNames.get(i), paramValues.get(i));
        }

        String lockParameterValue = Objects.requireNonNull(
                        spelExpressionParser.parseExpression(redisLock.lockParameter())
                                .getValue(context))
                .toString();

        Map<String, Object> result = new HashMap<>();
        result.put(RedisLockConstants.LOCK_NAME, redisLock.lockPrefix() + ":" + lockParameterValue);
        result.put(RedisLockConstants.LOCK_WAIT, redisLock.lockWait());
        result.put(RedisLockConstants.AUTO_UNLOCK_TIME, redisLock.autoUnlockTime());
        result.put(RedisLockConstants.RETRY_NUM, redisLock.retryNum());
        result.put(RedisLockConstants.RETRY_WAIT, redisLock.retryWait());
        return result;
    }
}
