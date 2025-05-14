package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.enums.DataEnums;
import com.health.remind.config.exception.DataException;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.RuleUser;
import com.health.remind.mapper.RuleUserMapper;
import com.health.remind.service.RuleUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public boolean verify(RuleTypeEnum ruleType, int num) {
        return switch (ruleType) {
            case limit_time_rule_num -> limitTimeRuleNumVerify(ruleType, num);
            case limit_open_task_num -> limitOpenTaskNumVerify(ruleType, num);
            case limit_continued_task_num -> limitContinuedTaskNumVerify(ruleType, num);
            case limit_remind_day_num -> limitRemindDayNumVerify(ruleType, num);
            case limit_remind_month_num -> limitRemindMonthNumVerify(ruleType, num);
            case limit_ads_num -> limitAdsNumVerify(ruleType, num);
        };
    }

    private boolean limitAdsNumVerify(RuleTypeEnum ruleType, int num) {
        Long account = CommonMethod.getAccount();

        return false;
    }

    private boolean limitRemindMonthNumVerify(RuleTypeEnum ruleType, int num) {
        return false;
    }

    private boolean limitRemindDayNumVerify(RuleTypeEnum ruleType, int num) {
        return false;
    }

    private boolean limitContinuedTaskNumVerify(RuleTypeEnum ruleType, int num) {
        return false;
    }

    private boolean limitOpenTaskNumVerify(RuleTypeEnum ruleType, int num) {
        return false;
    }

    private boolean limitTimeRuleNumVerify(RuleTypeEnum ruleType, int num) {
        return false;
    }


}
