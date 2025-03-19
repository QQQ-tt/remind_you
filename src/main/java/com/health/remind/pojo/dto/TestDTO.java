package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.Test;
import lombok.Getter;
import lombok.Setter;

/**
 * @author QQQtx
 * @since 2025/1/14 13:17
 */
@Getter
@Setter
public class TestDTO extends PageDTO<Test> {

    private String name;

    private Long size;
}
