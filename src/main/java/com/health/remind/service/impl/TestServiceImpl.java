package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.config.lock.RedisLock;
import com.health.remind.entity.Test;
import com.health.remind.mapper.TestMapper;
import com.health.remind.pojo.dto.TestDTO;
import com.health.remind.pojo.dto.TestEntityDTO;
import com.health.remind.service.TestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
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

    @RedisLock(lockPrefix = "testRedisLock", lockParameter = "#key")
    @Override
    public String testRedisLock(String key) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}
