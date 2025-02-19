package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.entity.SysRole;
import com.health.remind.pojo.dto.SysRoleDTO;
import com.health.remind.pojo.dto.SysRolePageDTO;
import com.health.remind.pojo.vo.SysRoleVO;
import com.health.remind.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统角色 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Tag(name = "系统角色")
@RestController
@RequestMapping("/remind/sysRole")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Operation(summary = "分页查询系统角色")
    @PostMapping("/pageSysRole")
    public R<Page<SysRoleVO>> pageSysRole(@RequestBody SysRolePageDTO dto) {
        return R.success(sysRoleService.pageSysRole(dto));
    }

    @Operation(summary = "新增或修改系统角色")
    @PutMapping("/saveOrUpdateSysRole")
    public R<Boolean> saveOrUpdateSysRole(@RequestBody SysRoleDTO dto) {
        return R.success(sysRoleService.saveOrUpdateSysRole(dto));
    }

    @Operation(summary = "根据id查询系统角色")
    @GetMapping("/getSysRoleById")
    public R<SysRole> getSysRoleById(@RequestBody Long id) {
        return R.success(sysRoleService.getSysRoleById(id));
    }

    @Operation(summary = "更新角色状态")
    @PutMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestBody SysRoleDTO dto) {
        return R.success(sysRoleService.updateStatus(dto.getId(), dto.getStatus()));
    }

    @Operation(summary = "删除系统角色")
    @DeleteMapping("/removeByRoleId")
    public R<Boolean> removeByRoleId(@RequestBody Long id) {
        return R.success(sysRoleService.removeByRoleId(id));
    }
}
