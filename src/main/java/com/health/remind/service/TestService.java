package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.entity.Test;
import com.health.remind.pojo.dto.TestDTO;
import com.health.remind.pojo.dto.TestEntityDTO;

/**
 * <p>
 * 测试表 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
public interface TestService extends IService<Test> {

    /**
     * 分页查询
     *
     * @param dto 分页参数
     * @return 分页结果
     */
    Page<Test> pageTest(TestDTO dto);

    /**
     * 新增或修改
     *
     * @param dto 新增或修改参数
     * @return 结果
     */
    boolean saveOrUpdateTest(TestEntityDTO dto);

    /**
     * 测试redis锁
     *
     * @param key 随机字符串
     * @return 结果
     */
    String testRedisLock(String key, Long length);

    /**
     * 测试cache,使用CACHE_10
     * 强制更新缓存（总是执行方法并更新） @CachePut
     *
     * @param value 测试值
     * @return 结果
     */
    String testCache(String value);

    /**
     * 测试cache,使用CACHE_10
     * 缓存数据，如果缓存存在则直接返回，否则执行方法并更新缓存 @Cacheable
     *
     * @param value 测试值
     * @return 结果
     */
    String testCache2(String value);

    /**
     * 测试cache,使用CACHE_5
     * 强制更新缓存（总是执行方法并更新） @CachePut
     *
     * @param value 测试值
     * @return 结果
     */
    String testCache3(String value);

    /**
     * 测试cache,使用CACHE_5
     * 缓存数据，如果缓存存在则直接返回，否则执行方法并更新缓存 @Cacheable
     *
     * @param value 测试值
     * @return 结果
     */
    String testCache4(String value);
}
