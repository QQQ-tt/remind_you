package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.StaticConstant;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.Test;
import com.health.remind.mapper.TestMapper;
import com.health.remind.pojo.dto.TestDTO;
import com.health.remind.pojo.dto.TestEntityDTO;
import com.health.remind.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
@Slf4j
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    @Override
    public Page<Test> pageTest(TestDTO dto) {
        return page(dto.getPage(), Wrappers.lambdaQuery(Test.class)
                .eq(StringUtils.isNotBlank(dto.getName()), Test::getName, dto.getName()));
    }

    @Override
    public boolean saveOrUpdateTest(TestEntityDTO dto) {
        long count = count(Wrappers.lambdaQuery(Test.class)
                .eq(StringUtils.isNotBlank(dto.getName()), Test::getName,
                        dto.getName())
                .ne(dto.getId() != null, Test::getId, dto.getId()));
        if (count == 0) {
            return saveOrUpdate(Test.builder()
                    .name(dto.getName())
                    .num(dto.getNum())
                    .build());
        }
        throw new DataException(DataEnums.DATA_IS_ABNORMAL);
    }

    //    @RedisLock(lockParameter = "#key")
    @RedisLock(lockParameter = "T(com.health.remind.config.CommonMethod).getUserId()", autoUnlockTime = 60000000)
    @Override
    public String testRedisLock(String key, Long length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return key;
    }

    @CachePut(value = StaticConstant.CACHE_10, key = "#value")
    @Override
    public String testCache(String value) {
        log.info("10:CachePut From Database: {}", value);
        return value;
    }

    @Cacheable(value = StaticConstant.CACHE_10, key = "#value")
    @Override
    public String testCache2(String value) {
        log.info("10:Cacheable From Database: {}", value);
        return value;
    }

    @CachePut(value = StaticConstant.CACHE_5, key = "#value")
    @Override
    public String testCache3(String value) {
        log.info("5:CachePut From Database: {}", value);
        return value;
    }

    @Cacheable(value = StaticConstant.CACHE_5, key = "#value")
    @Override
    public String testCache4(String value) {
        log.info("5:Cacheable From Database: {}", value);
        return value;
    }
}
