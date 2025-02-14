package com.health.remind.controller;

import com.health.remind.config.R;
import com.health.remind.pojo.dto.SignDTO;
import com.health.remind.pojo.vo.SignVO;
import com.health.remind.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
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

    @Operation(summary = "用户注册")
    @PutMapping("/signUser")
    public R<SignVO> signUser(@RequestBody SignDTO dto) {
        return R.success(sysUserService.signUser(dto));
    }
}
