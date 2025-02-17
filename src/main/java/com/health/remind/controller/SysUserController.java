package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.dto.SysUserDTO;
import com.health.remind.pojo.dto.SysUserPageDTO;
import com.health.remind.pojo.vo.LoginDTO;
import com.health.remind.pojo.vo.LoginVO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.pojo.vo.SysUserVO;
import com.health.remind.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Operation(summary = "用户注册（管理端）")
    @PutMapping("/signUser")
    public R<SignVO> signUser(@RequestBody @Valid SignDTO dto) {
        return R.success(sysUserService.signUser(dto));
    }

    @Operation(summary = "用户登录（管理端）")
    @PostMapping("/loginUser")
    public R<LoginVO> loginUser(@RequestBody @Valid LoginDTO dto) {
        return R.success(sysUserService.loginUser(Long.parseLong(dto.getAccount()), dto.getPassword()));
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageSysUser")
    public R<Page<SysUserVO>> pageSysUser(@RequestBody SysUserPageDTO dto) {
        return R.success(sysUserService.pageSysUser(dto));
    }

    @Operation(summary = "保存或编辑用户")
    @PostMapping("/saveOrUpdateSysUser")
    public R<Boolean> saveOrUpdateSysUser(@RequestBody @Valid SysUserDTO dto) {
        return R.success(sysUserService.saveOrUpdateSysUser(dto));
    }
}
