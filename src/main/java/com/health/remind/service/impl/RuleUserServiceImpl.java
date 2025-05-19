package com.health.remind.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.RuleUser;
import com.health.remind.entity.SysUser;
import com.health.remind.mapper.RuleUserMapper;
import com.health.remind.pojo.bo.RuleUserRedisBO;
import com.health.remind.service.RuleTemplateService;
import com.health.remind.service.RuleUserService;
import com.health.remind.util.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final RuleTemplateService ruleTemplateService;

    public RuleUserServiceImpl(RuleTemplateService ruleTemplateService) {
        this.ruleTemplateService = ruleTemplateService;
    }

    @Override
    public List<RuleUser> getRuleUserByUserId(Long userId) {
        String ruleUser = RedisKeys.getRuleUser(CommonMethod.getAccount(), StaticConstant.USER_TYPE_SYS);
        if (RedisUtils.hasKey(ruleUser)) {
            return RedisUtils.getObject(ruleUser, new TypeReference<>() {
            });
        }
        List<RuleUser> list = list(Wrappers.lambdaQuery(RuleUser.class)
                .eq(RuleUser::getSysUserId, userId)
                .le(RuleUser::getStartedAt, LocalDateTime.now())
                .gt(RuleUser::getExpiredAt, LocalDateTime.now()));
        RedisUtils.setObject(ruleUser, list);
        setExpireTime(ruleUser);
        return list;
    }

    @Override
    public Map<RuleTypeEnum, RuleUserRedisBO> getRuleUser() {
        String ruleUser = RedisKeys.getRuleUser(CommonMethod.getAccount(), StaticConstant.USER_TYPE_APP);
        if (RedisUtils.hasKey(ruleUser)) {
            return RedisUtils.getObject(ruleUser, new TypeReference<>() {
            });
        }
        List<RuleUser> list = list(Wrappers.lambdaQuery(RuleUser.class)
                .eq(RuleUser::getSysUserId, CommonMethod.getUserId())
                .le(RuleUser::getStartedAt, LocalDateTime.now())
                .gt(RuleUser::getExpiredAt, LocalDateTime.now()));
        return setRedis(list, ruleUser);
    }

    @Override
    @Async("customExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void saveRuleByUser(SysUser sysUser) {
        List<RuleTemplate> list = ruleTemplateService.list(Wrappers.lambdaQuery(RuleTemplate.class)
                .eq(RuleTemplate::getStatus, true)
                .eq(RuleTemplate::getInterestsLevel, InterestsLevelEnum.VIP_0));
        saveRuleByUserId(sysUser.getId(), list, Boolean.TRUE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRuleByUserId(Long userId, List<RuleTemplate> rules, Boolean isRedis) {
        Long l =
                Optional.of(userId != null ? userId : CommonMethod.getUserId())
                        .orElseThrow(() -> new DataException(
                                DataEnums.USER_NOT_LOGIN));
        List<RuleUser> list = new ArrayList<>();
        rules.forEach(e -> {
            RuleUser ruleUser = new RuleUser();
            ruleUser.setRuleTemplateId(e.getId());
            ruleUser.setSysUserId(l);
            ruleUser.setRuleType(e.getRuleType());
            ruleUser.setName(e.getName());
            ruleUser.setValue(e.getValue());
            ruleUser.setPriority(e.getPriority());
            setTime(ruleUser, e, isRedis);
            list.add(ruleUser);
        });
        if (isRedis) {
            setRedis(list, RedisKeys.getRuleUser(CommonMethod.getAccount(), StaticConstant.USER_TYPE_APP));
        }
        saveBatch(list);
    }

    private Map<RuleTypeEnum, RuleUserRedisBO> setRedis(List<RuleUser> list, String ruleUser) {
        Map<RuleTypeEnum, RuleUserRedisBO> result = list.stream()
                .collect(Collectors.groupingBy(
                        RuleUser::getRuleType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                ruleUsers -> {
                                    RuleUserRedisBO merged = new RuleUserRedisBO();
                                    for (RuleUser ru : ruleUsers) {
                                        if (StringUtils.isNotBlank(ru.getName())) {
                                            merged.setName(ru.getName());
                                        }
                                        merged.setValue(
                                                (merged.getValue() == null ? 0 : merged.getValue()) + (ru.getValue() == null ? 0 : ru.getValue()));
                                        merged.setUseValue(
                                                (merged.getUseValue() == null ? 0 : merged.getUseValue()) + (ru.getUseValue() == null ? 0 : ru.getUseValue()));
                                    }
                                    return merged;
                                }
                        )
                ));
        result.forEach((k, v) -> RedisUtils.hPut(ruleUser, k.toString(), JSONObject.toJSONString(v)));
        setExpireTime(ruleUser);
        return result;
    }


    private void setTime(RuleUser ruleUser, RuleTemplate ruleTemplate, Boolean init) {
        Optional.ofNullable(ruleTemplate.getExpiredPeriodValue())
                .ifPresentOrElse(i -> {
                    switch (ruleTemplate.getExpiredPeriodType()) {
                        case RELATIVE_TIME -> {
                            ruleUser.setStartedAt(LocalDateTime.now());
                            switch (ruleTemplate.getExpiredPeriodUnit()) {
                                case DAY -> ruleUser.setExpiredAt(LocalDateTime.now()
                                        .plusDays(i));
                                case MONTH -> ruleUser.setExpiredAt(LocalDateTime.now()
                                        .plusMonths(i));
                            }
                        }
                        case ABSOLUTE_TIME -> {
                            if (init) {
                                switch (ruleTemplate.getExpiredPeriodUnit()) {
                                    case DAY -> {
                                        ruleUser.setStartedAt(LocalDateTime.now());
                                        ruleUser.setExpiredAt(getNextFour(LocalTime.now(), i));
                                    }
                                    case MONTH -> {
                                        ruleUser.setStartedAt(LocalDateTime.now());
                                        ruleUser.setExpiredAt(getNextMonthOne(LocalDateTime.now(), i));
                                    }
                                }
                            } else {
                                switch (ruleTemplate.getExpiredPeriodUnit()) {
                                    case DAY -> {
                                        ruleUser.setStartedAt(getNextFour(LocalTime.now(), i));
                                        ruleUser.setExpiredAt(ruleUser.getStartedAt()
                                                .plusDays(1));
                                    }
                                    case MONTH -> {
                                        ruleUser.setStartedAt(getNextMonthOne(LocalDateTime.now(), i));
                                        ruleUser.setExpiredAt(ruleUser.getStartedAt()
                                                .plusMonths(1));
                                    }
                                }
                            }
                        }
                    }
                }, () -> {
                    ruleUser.setStartedAt(LocalDateTime.now());
                    ruleUser.setExpiredAt(LocalDateTime.of(LocalDate.of(2099, 12, 31), LocalTime.of(23, 59, 59)));
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verify(RuleTypeEnum ruleType, int num) {
        if (CommonMethod.getUserType()
                .equals(StaticConstant.USER_TYPE_SYS)) {
            return;
        }
        Long account = CommonMethod.getAccount();
        String redisKey = RedisKeys.getRuleUser(account, StaticConstant.USER_TYPE_APP);
        Object o = RedisUtils.hGet(redisKey, ruleType.toString());
        RuleUserRedisBO ruleUser;
        LocalDateTime now = LocalDateTime.now();
        List<RuleUser> value = list(Wrappers.lambdaQuery(RuleUser.class)
                .eq(RuleUser::getSysUserId, CommonMethod.getUserId())
                .eq(RuleUser::getRuleType, ruleType)
                .le(RuleUser::getStartedAt, now)
                .ge(RuleUser::getExpiredAt, now)
                .orderByAsc(RuleUser::getPriority, RuleUser::getExpiredAt));// 优先级+快过期的先用
        if (value.isEmpty()) {
            // 非活跃用户
            List<RuleTemplate> list = ruleTemplateService.list(Wrappers.lambdaQuery(RuleTemplate.class)
                    .eq(RuleTemplate::getStatus, Boolean.TRUE)
                    .eq(RuleTemplate::getRuleType, ruleType)
                    .eq(RuleTemplate::getInterestsLevel, CommonMethod.getInterestsLevel()));
            list.forEach(e -> {
                RuleUser r = new RuleUser();
                r.setRuleTemplateId(e.getId());
                r.setSysUserId(CommonMethod.getUserId());
                r.setName(e.getName());
                r.setValue(e.getValue());
                r.setPriority(e.getPriority());
                setTime(r, e, true);
                value.add(r);
            });
        }
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
        RedisUtils.hPut(redisKey, ruleType.toString(), JSONObject.toJSONString(ruleUser));
        setExpireTime(redisKey);
    }

    /**
     * 过期时间设置为下一次4点
     *
     * @param redisKey redis_key
     */
    private static void setExpireTime(String redisKey) {
        LocalTime now = LocalTime.now(); // 当前时间
        LocalDateTime nextFour = getNextFour(now, 4);
        Duration duration = Duration.between(LocalDateTime.now(), nextFour);
        long seconds = duration.getSeconds();
        RedisUtils.expire(redisKey, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取下一次i点的时间
     *
     * @param now 当前时间
     * @return 下一次4点时间
     */
    private static LocalDateTime getNextFour(LocalTime now, int i) {
        LocalDateTime nextFour = LocalDateTime.of(LocalDate.now(), LocalTime.of(i, 0));
        // 如果当前时间在i点之后，则取明天的i点；否则取今天的i点
        if (now.getHour() > i || (now.getHour() == i && now.getMinute() > 0)) {
            nextFour = nextFour.plusDays(1);
        }
        return nextFour;
    }

    /**
     * 获取下一次的某日4点
     *
     * @param now 当前时间
     * @return 下个月的1号4点
     */
    private static LocalDateTime getNextMonthOne(LocalDateTime now, int i) {
        LocalDateTime localDateTime = now.withDayOfMonth(i)
                .withHour(4)
                .withMinute(0)
                .withSecond(0);
        if (now.getHour() > 4 || (now.getHour() == 4 && now.getMinute() > 0)) {
            return localDateTime
                    .plusMonths(1);
        }
        return localDateTime;
    }
}
