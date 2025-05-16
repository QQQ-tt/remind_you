package com.health.remind.common.enums;

/**
 * 权益规则枚举
 *
 * @author qtx
 * @since 2025/5/14 10:04
 */
public enum RuleTypeEnum {

    /**
     * 每天创建任务数量
     */
    limit_create_task_day_num,
    /**
     * 开启中任务数量
     */
    limit_open_task_num,
    /**
     * 提醒规则数量
     */
    limit_time_rule_num,
    /**
     * 每天提醒次数
     */
    limit_remind_day_num,
    /**
     * 每月提醒次数
     */
    limit_remind_month_num,
    /**
     * 播放广告次数
     */
    limit_ads_num,
}
