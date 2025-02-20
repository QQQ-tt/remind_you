package com.health.remind.common.enums;

import lombok.Getter;

/**
 * @author QQQtx
 * @since 2025/2/20
 */
@Getter
public enum SysResourceEnum {

    route(0, "路由"),
    api(1, "接口");

    private final Integer type;
    private final String desc;

    SysResourceEnum(int i, String s) {
        this.type = i;
        this.desc = s;
    }
}
