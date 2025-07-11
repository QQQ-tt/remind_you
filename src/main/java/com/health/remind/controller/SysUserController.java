package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.common.StaticConstant;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.R;
import com.health.remind.entity.SysUser;
import com.health.remind.pojo.dto.AppUserDTO;
import com.health.remind.pojo.dto.AppUserEmailDTO;
import com.health.remind.pojo.dto.LoginAppDTO;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.dto.SysUserDTO;
import com.health.remind.pojo.dto.SysUserPageDTO;
import com.health.remind.pojo.dto.SysUserRulePageDTO;
import com.health.remind.pojo.vo.LoginDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.pojo.vo.SysUserRuleVO;
import com.health.remind.pojo.vo.SysUserVO;
import com.health.remind.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/remind/sysUser")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "用户注册（管理端）", description = StaticConstant.PERMISSION_KEY)
    @PutMapping("/signUser")
    public R<SignVO> signUser(@RequestBody @Valid SignDTO dto) {
        return R.success(sysUserService.signUser(dto));
    }

    @Operation(summary = "用户登录（管理端）", description = StaticConstant.PERMISSION_KEY)
    @PostMapping("/loginUser")
    public R<LoginVO> loginUser(@RequestBody @Valid LoginDTO dto) {
        return R.success(sysUserService.loginUser(Long.parseLong(dto.getAccount()), dto.getPassword()));
    }

    @Operation(summary = "用户登录（用户端）", description = StaticConstant.PERMISSION_KEY)
    @PostMapping("/loginAppUser")
    public R<LoginVO> loginAppUser(@RequestBody LoginAppDTO dto) {
        return R.success(sysUserService.loginAppUser(dto));
    }

    @Operation(summary = "刷新token")
    @GetMapping("/refreshToken")
    public R<LoginVO> refreshToken(@RequestParam String token) {
        return R.success(sysUserService.refreshToken(token));
    }

    @Operation(summary = "消息数量")
    @GetMapping("/getMsgNum")
    public R<Integer> getMsgNum() {
        return R.success(sysUserService.getMsgNum());
    }

    @Operation(summary = "添加消息")
    @GetMapping("/increasePushCount")
    public R<Integer> increasePushCount() {
        return R.success(sysUserService.increasePushCount());
    }

    @Operation(summary = "测试消息")
    @GetMapping("/testMsg")
    public R<Integer> testMsg() {
        return R.success(sysUserService.testMsg());
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageSysUser")
    public R<Page<SysUserVO>> pageSysUser(@RequestBody @Valid SysUserPageDTO dto) {
        return R.success(sysUserService.pageSysUser(dto));
    }

    @Operation(summary = "分页查询用户权益")
    @PostMapping("/pageSysUserRule")
    public R<Page<SysUserRuleVO>> pageSysUserRule(@RequestBody SysUserRulePageDTO dto) {
        return R.success(sysUserService.pageSysUserRule(dto));
    }

    @Operation(summary = "保存或编辑用户")
    @PutMapping("/saveOrUpdateSysUser")
    public R<Boolean> saveOrUpdateSysUser(@RequestBody @Valid SysUserDTO dto) {
        return R.success(sysUserService.saveOrUpdateSysUser(dto));
    }

    @Operation(summary = "更新用户")
    @PutMapping("/updateAppUser")
    public R<Boolean> updateAppUser(@RequestBody @Valid AppUserDTO dto) {
        return R.success(sysUserService.updateAppUser(dto));
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public R<Boolean> sendEmailCode(@RequestBody @Valid AppUserEmailDTO dto) {
        sysUserService.sendEmailCode(dto.getEmail());
        return R.success();
    }

    @Operation(summary = "修改邮箱")
    @PutMapping("/updateEmail")
    public R<String> updateEmail(@RequestBody @Valid AppUserEmailDTO dto) {
        return sysUserService.updateEmail(dto.getEmail(), dto.getCaptchaCode()) ? R.success() : R.verifyFailed(
                "修改邮箱失败");
    }

    @Operation(summary = "根据账号查询邮箱")
    @GetMapping("/getAppSysUserByAccount")
    public R<String> getAppSysUserByAccount() {
        return R.success(sysUserService.getAppSysUserByAccount(CommonMethod.getAccount())
                .getEmail());
    }

    @Operation(summary = "取消角色")
    @PutMapping("/cancelRole")
    public R<Boolean> cancelRole(@RequestParam Long id) {
        return R.success(sysUserService.cancelRole(id));
    }

    @Operation(summary = "根据id查询用户")
    @GetMapping("/getSysUserById")
    public R<SysUser> getSysUserById(@RequestParam Long id) {
        return R.success(sysUserService.getSysUserById(id));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/removeByUserId")
    public R<Boolean> removeByUserId(@RequestParam Long id) {
        return R.success(sysUserService.removeByUserId(id));
    }

    @Operation(summary = "测试token")
    @GetMapping("/testToken")
    public R<Boolean> testToken() {
        return R.success(sysUserService.testToken());
    }
}
