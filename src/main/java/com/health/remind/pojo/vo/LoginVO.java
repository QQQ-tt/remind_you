package com.health.remind.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qtx
 * @since 2025/2/16 20:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private Long id;

    private String name;

    private String token;
}
