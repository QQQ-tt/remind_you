package com.health.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.RuleUser;
import com.health.remind.entity.SysUser;
import com.health.remind.pojo.bo.RuleUserRedisBO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
public interface RuleUserService extends IService<RuleUser> {

    /**
     * 根据用户id获取用户规则
     *
     * @param userId 用户id
     * @return 用户规则
     */
    List<RuleUser> getRuleUserByUserId(Long userId);

    /**
     * 获取用户规则
     *
     * @return 用户规则
     */
    List<RuleUserRedisBO> getRuleUser();

    /**
     * 保存用户规则
     *
     * @param sysUser 用户信息
     */
    void saveRuleByUser(SysUser sysUser);

    /**
     * 保存用户规则
     *
     * @param userId  用户id(用户id为空自动获取token用户id)
     * @param rules   用户规则
     * @param isRedis 是否保存到redis
     */
    void saveRuleByUserId(Long userId, List<RuleTemplate> rules, Boolean isRedis);

    /**
     * 验证用户权益规则
     *
     * @param ruleType 规则类型
     * @param num      数量
     */
    void verify(RuleTypeEnum ruleType, int num);
}
