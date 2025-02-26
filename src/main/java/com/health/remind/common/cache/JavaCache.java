package com.health.remind.common.cache;

import com.health.remind.common.StaticConstant;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/2/26
 */
@Slf4j
@Component
public class JavaCache {

    @CachePut(value = StaticConstant.CACHE_10, key = "#roleId")
    public CommonMethod.TrieNode getTrieNode(Long roleId, List<String> urls) {
        log.info("10:CachePut Create TrieNode: {}", roleId);
        CommonMethod.TrieNode node = new CommonMethod.TrieNode();
        CommonMethod.addPatterns(urls, node);
        return node;
    }

    @Cacheable(value = StaticConstant.CACHE_10, key = "#roleId")
    public CommonMethod.TrieNode verify(Long roleId) {
        log.info("10:Cacheable From Redis: {}", roleId);
        return RedisUtils.getObject(RedisKeys.getRoleResourceKey(roleId),
                CommonMethod.TrieNode.class);
    }
}
