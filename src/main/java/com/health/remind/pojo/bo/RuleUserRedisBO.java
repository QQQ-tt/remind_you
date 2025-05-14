package com.health.remind.pojo.bo;

import lombok.Data;

/**
 * @author qtx
 * @since 2025/5/14 22:54
 */
@Data
public class RuleUserRedisBO {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 次数总值
     */
    private Integer value;

    /**
     * 已用次数总值
     */
    private Integer useValue;
}
