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
import com.health.remind.service.RuleUserService;
import com.health.remind.util.RedisUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    public boolean verify(RuleTypeEnum ruleType, int num) {
        Long account = CommonMethod.getAccount();
        String s = (String) RedisUtils.hGet(RedisKeys.getRuleUser(account), ruleType.toString());
        AtomicReference<RuleUser> ruleUser = new AtomicReference<>();
        if (s == null) {
            List<RuleUser> list = list(Wrappers.lambdaQuery(RuleUser.class)
                    .eq(RuleUser::getSysUserId, CommonMethod.getUserId())
                    .eq(RuleUser::getRuleType, ruleType));
            if (list.isEmpty()) {
                return false;
            }
            Integer i = list.stream()
                    .map(RuleUser::getValue)
                    .reduce(Integer::sum)
                    .orElse(0);
            list.stream().findFirst().ifPresent(e -> {
                ruleUser.set(e);
                ruleUser.get().setValue(i);
            });
        } else {
            ruleUser.set(JSONObject.parseObject(s, RuleUser.class));
        }
        if (ruleUser.get() == null) {
            return false;
        }
        if (ruleUser.get()
                .getUseValue() + num <= ruleUser.get()
                .getValue()) {
            ruleUser.get()
                    .setUseValue(ruleUser.get()
                            .getUseValue() + num);
            RedisUtils.hPut(RedisKeys.getRuleUser(account), ruleType.toString(), JSONObject.toJSONString(ruleUser));
            update(Wrappers.lambdaUpdate(RuleUser.class)
                    .eq(RuleUser::getSysUserId, CommonMethod.getUserId())
                    .eq(RuleUser::getRuleTemplateId, ruleUser.get()
                            .getRuleTemplateId())
                    .set(RuleUser::getUseValue, ruleUser.get()
                            .getUseValue()));
            return true;
        }
        return false;
    }

}
