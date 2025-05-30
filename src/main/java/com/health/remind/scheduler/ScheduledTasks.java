package com.health.remind.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.health.remind.common.StaticConstant;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.common.enums.RuleExpiredTypeEnum;
import com.health.remind.common.enums.RuleExpiredUnitEnum;
import com.health.remind.common.keys.RedisKeys;
import com.health.remind.config.BaseEntity;
import com.health.remind.entity.RemindTaskInfo;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.entity.SysUser;
import com.health.remind.mail.MailService;
import com.health.remind.service.RemindTaskInfoService;
import com.health.remind.service.RuleTemplateService;
import com.health.remind.service.RuleUserService;
import com.health.remind.service.SysRoleResourceService;
import com.health.remind.service.SysUserService;
import com.health.remind.util.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Slf4j
@Component
public class ScheduledTasks {

    private final SysRoleResourceService sysRoleResourceService;

    private final SysUserService sysUserService;

    private final RuleTemplateService ruleTemplateService;

    private final RuleUserService ruleUserService;

    private final MailService mailService;

    private final RemindTaskInfoService remindTaskInfoService;

    public ScheduledTasks(SysRoleResourceService sysRoleResourceService, SysUserService sysUserService, RuleTemplateService ruleTemplateService, RuleUserService ruleUserService, MailService mailService, RemindTaskInfoService remindTaskInfoService) {
        this.sysRoleResourceService = sysRoleResourceService;
        this.sysUserService = sysUserService;
        this.ruleTemplateService = ruleTemplateService;
        this.ruleUserService = ruleUserService;
        this.mailService = mailService;
        this.remindTaskInfoService = remindTaskInfoService;
    }

    /**
     * 定时初始化用户权益
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void initUserInterests() {
        log.info("开始初始化用户权益");
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
        log.info("初始化用户数量:{},权益数量:{}", list.size(), templateList.size());
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
    @PostConstruct
    @Scheduled(cron = "0 0 1 * * *")
    public void initRoleResource() {
        RedisUtils.delete(RedisUtils.keys(RedisKeys.getRoleResourceKey(null)));
        sysRoleResourceService.listRoleResourceByRoleId(new ArrayList<>());
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendSystemReports() {
        log.info("开始发送系统报告");
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now()
                .minusDays(1), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now()
                .minusDays(1), LocalTime.MAX);
        long count = sysUserService.count(Wrappers.lambdaQuery(SysUser.class)
                .eq(SysUser::getUserType,
                        StaticConstant.USER_TYPE_APP)
                .between(BaseEntity::getCreateTime,
                        startTime,
                        endTime));
        long count1 = remindTaskInfoService.count(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .between(RemindTaskInfo::getEstimatedTime, startTime, endTime));
        long count2 = remindTaskInfoService.count(Wrappers.lambdaQuery(RemindTaskInfo.class)
                .eq(RemindTaskInfo::getIsSend, true)
                .between(RemindTaskInfo::getEstimatedTime, startTime, endTime));
        log.info("新增用户数量:{},新增任务数量:{},已发送任务数量:{}", count, count1, count2);
        String subject = "系统日报 - " + LocalDate.now().minusDays(1);
        String content = String.format(
                "以下是昨日（%s）的系统数据报告：<br><br>" +
                        "新增用户数量：%d<br>" +
                        "新增任务数量：%d<br>" +
                        "已发送任务数量：%d<br><br>" +
                        "感谢您的查看！",
                LocalDate.now().minusDays(1), count, count1, count2);

        mailService.send("1102214883@qq.com", subject, content);
    }
}
