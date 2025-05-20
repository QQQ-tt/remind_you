package com.health.remind.controller;

import com.health.remind.config.R;
import com.health.remind.entity.RuleUser;
import com.health.remind.entity.SysUser;
import com.health.remind.pojo.bo.RuleUserRedisBO;
import com.health.remind.service.RuleUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Tag(name = "用户权益详情")
@RestController
@RequestMapping("/remind/ruleUser")
public class RuleUserController {

    private final RuleUserService ruleUserService;

    public RuleUserController(RuleUserService ruleUserService) {
        this.ruleUserService = ruleUserService;
    }

    @Operation(summary = "根据用户id查询用户权益详情")
    @GetMapping("/getRuleUserByUserId")
    public R<List<RuleUser>> getRuleUserByUserId(@RequestParam Long userId) {
        return R.success(ruleUserService.getRuleUserByUserId(userId));
    }

    @Operation(summary = "查询用户权益详情")
    @GetMapping("/getRuleUser")
    public R<List<RuleUserRedisBO>> getRuleUser() {
        return R.success(ruleUserService.getRuleUser());
    }

    @Operation(summary = "保存用户权益详情")
    @PutMapping("/saveRuleByUser")
    public R<Void> saveRuleByUser(@RequestBody SysUser sysUser) {
        ruleUserService.saveRuleByUser(sysUser);
        return R.success();
    }
}
