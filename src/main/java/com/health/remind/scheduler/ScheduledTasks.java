package com.health.remind.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.common.enums.RuleExpiredTypeEnum;
import com.health.remind.common.enums.RuleExpiredUnitEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.SysUser;
import com.health.remind.service.RuleTemplateService;
import com.health.remind.service.RuleUserService;
import com.health.remind.service.SysRoleResourceService;
import com.health.remind.service.SysUserService;
import com.health.remind.util.RedisUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Component
public class ScheduledTasks {

    private final SysRoleResourceService sysRoleResourceService;

    private final SysUserService sysUserService;

    private final RuleTemplateService ruleTemplateService;

    private final RuleUserService ruleUserService;

    public ScheduledTasks(SysRoleResourceService sysRoleResourceService, SysUserService sysUserService, RuleTemplateService ruleTemplateService, RuleUserService ruleUserService) {
        this.sysRoleResourceService = sysRoleResourceService;
        this.sysUserService = sysUserService;
        this.ruleTemplateService = ruleTemplateService;
        this.ruleUserService = ruleUserService;
    }

    /**
     * 定时初始化用户权益
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void initUserInterests() {
        List<SysUser> list = sysUserService.list(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUserType, StaticConstant.USER_TYPE_APP)
                .eq(SysUser::getStatus, Boolean.TRUE)
                .ge(SysUser::getLoginTime, LocalDateTime.now()
                        .minusMonths(1)));
        if (list.isEmpty()) {
            return;
        }
        List<RuleTemplate> templateList = ruleTemplateService.list(Wrappers.lambdaQuery(RuleTemplate.class)
                .eq(RuleTemplate::getStatus, Boolean.TRUE)
                .eq(RuleTemplate::getExpiredPeriodUnit, RuleExpiredUnitEnum.DAY)
                .eq(RuleTemplate::getExpiredPeriodType, RuleExpiredTypeEnum.ABSOLUTE_TIME));
        Map<InterestsLevelEnum, List<RuleTemplate>> enumListMap = templateList.stream()
                .collect(Collectors.groupingBy(RuleTemplate::getInterestsLevel));
        list.forEach(sysUser -> {
            switch (sysUser.getInterestsLevel()) {
                case VIP_1 -> ruleUserService.saveRuleByUserId(sysUser.getId(),
                        enumListMap.get(InterestsLevelEnum.VIP_1), Boolean.FALSE);
                case VIP_2 -> ruleUserService.saveRuleByUserId(sysUser.getId(),
                        enumListMap.get(InterestsLevelEnum.VIP_2), Boolean.FALSE);
                default -> ruleUserService.saveRuleByUserId(sysUser.getId(),
                        enumListMap.get(InterestsLevelEnum.VIP_0), Boolean.FALSE);
            }
        });
    }

    /**
     * 定时删除过期数据
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void removeExpiredData() {
    }

    /**
     * 定时初始化角色资源
     */
    @Scheduled(cron = "0 0 1 * * *")
    @PostConstruct
    public void initRoleResource() {
        RedisUtils.delete(RedisUtils.keys(RedisKeys.getRoleResourceKey(null)));
        sysRoleResourceService.listRoleResourceByRoleId(new ArrayList<>());
    }
}
