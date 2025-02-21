package com.health.remind.controller;

import com.health.remind.config.R;
import com.health.remind.pojo.dto.SysRoleResourceDTO;
import com.health.remind.service.SysRoleResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色资源关联表 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Tag(name = "角色资源关联表")
@RestController
@RequestMapping("/remind/sysRoleResource")
public class SysRoleResourceController {

    private final SysRoleResourceService sysRoleResourceService;

    public SysRoleResourceController(SysRoleResourceService sysRoleResourceService) {
        this.sysRoleResourceService = sysRoleResourceService;
    }

    @Operation(summary = "保存角色资源关联表")
    @PostMapping("/saveRoleResource")
    public R<Boolean> saveRoleResource(@RequestBody @Valid SysRoleResourceDTO list) {
        return R.success(sysRoleResourceService.saveRoleResource(list));
    }

    @Operation(summary = "根据角色id查询角色资源关联表")
    @GetMapping("/listRoleResourceByRoleId")
    public R<List<Long>> listRoleResourceByRoleId(@RequestParam Long roleId) {
        return R.success(sysRoleResourceService.listRoleResourceByRoleId(roleId));
    }
}
