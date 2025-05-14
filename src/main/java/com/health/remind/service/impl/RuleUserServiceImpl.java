package com.health.remind.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.RuleUser;
import com.health.remind.mapper.RuleUserMapper;
import com.health.remind.pojo.bo.RuleUserRedisBO;
import com.health.remind.service.RuleUserService;
import com.health.remind.util.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Service
public class RuleUserServiceImpl extends ServiceImpl<RuleUserMapper, RuleUser> implements RuleUserService {

    @Override
    public List<RuleUser> getRuleUserByUserId(Long userId) {
        Long l =
                Optional.of(userId != null ? userId : CommonMethod.getUserId())
                        .orElseThrow(() -> new DataException(
                                DataEnums.USER_NOT_LOGIN));
        return list(Wrappers.lambdaQuery(RuleUser.class)
                .eq(RuleUser::getSysUserId, l)
                .le(RuleUser::getStartedAt, LocalDateTime.now())
                .gt(RuleUser::getExpiredAt, LocalDateTime.now()));
    }

    @Override
    public boolean saveRuleByUserId(Long userId, List<RuleTemplate> rules) {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verify(RuleTypeEnum ruleType, int num) {
        Long account = CommonMethod.getAccount();
        Object o = RedisUtils.hGet(RedisKeys.getRuleUser(account), ruleType.toString());
        RuleUserRedisBO ruleUser;
        LocalDateTime now = LocalDateTime.now();
        List<RuleUser> value = list(Wrappers.lambdaQuery(RuleUser.class)
                .eq(RuleUser::getSysUserId, CommonMethod.getUserId())
                .eq(RuleUser::getRuleType, ruleType)
                .le(RuleUser::getStartedAt, now)
                .ge(RuleUser::getExpiredAt, now)
                .orderByAsc(RuleUser::getPriority, RuleUser::getExpiredAt));// 优先级+快过期的先用)
        List<RuleUser> ruleUsers = new ArrayList<>();
        if (o != null) {
            ruleUser = JSONObject.parseObject((String) o, RuleUserRedisBO.class);
            if (ruleUser.getUseValue() + num <= ruleUser.getValue()) {
                ruleUser.setUseValue(ruleUser.getUseValue() + num);
            } else {
                throw new DataException(DataEnums.USER_RESOURCE_ERROR);
            }
            for (RuleUser rule : value) {
                int available = rule.getValue() - rule.getUseValue();
                if (available >= num) {
                    rule.setUseValue(rule.getUseValue() + num);
                    ruleUsers.add(rule);
                    break;
                } else {
                    rule.setUseValue(rule.getValue());
                    ruleUsers.add(rule);
                    num -= available;
                }
            }
        } else {
            int allValue = 0;
            int allUseValue = 0;
            ruleUser = new RuleUserRedisBO();
            for (RuleUser rule : value) {
                ruleUser.setName(rule.getName());
                allValue += rule.getValue();
                int available = rule.getValue() - rule.getUseValue();
                if (num > 0) {
                    if (available >= num) {
                        rule.setUseValue(rule.getUseValue() + num);
                        num = 0;
                    } else {
                        rule.setUseValue(rule.getValue());
                        num -= available;
                    }
                    ruleUsers.add(rule);
                }
                allUseValue += rule.getUseValue();
            }
            ruleUser.setValue(allValue);
            ruleUser.setUseValue(allUseValue);
        }
        if (num > 0) {
            throw new DataException(DataEnums.USER_RESOURCE_ERROR);
        }
        updateBatchById(ruleUsers);
        RedisUtils.hPut(RedisKeys.getRuleUser(account), ruleType.toString(), JSONObject.toJSONString(ruleUser));
    }
}
