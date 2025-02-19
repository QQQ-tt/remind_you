package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.entity.SysResource;
import com.health.remind.pojo.dto.SysResourceDTO;
import com.health.remind.pojo.dto.SysResourcePageDTO;
import com.health.remind.pojo.vo.SysResourceVO;
import com.health.remind.service.SysResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 系统资源 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Tag(name = "系统资源管理")
@RestController
@RequestMapping("/remind/sysResource")
public class SysResourceController {

    private final SysResourceService sysResourceService;

    public SysResourceController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageResource")
    public R<Page<SysResourceVO>> pageResource(@RequestBody SysResourcePageDTO dto) {
        return R.success(sysResourceService.pageResource(dto));
    }

    @Operation(summary = "根据pid查询资源")
    @PostMapping("/listResourceByParentId")
    public R<List<SysResource>> saveOrUpdate(@RequestParam Long id) {
        return R.success(sysResourceService.listResourceByParentId(id));
    }

    @Operation(summary = "根据id查询资源")
    @GetMapping("/getResourceById")
    public R<SysResource> getResourceById(@RequestParam Long id) {
        return R.success(sysResourceService.getResourceById(id));
    }

    @Operation(summary = "保存或编辑资源")
    @PutMapping("/saveOrUpdateResource")
    public R<Boolean> saveOrUpdateResource(@RequestBody SysResourceDTO dto) {
        return R.success(sysResourceService.saveOrUpdateResource(dto));
    }

    @Operation(summary = "更新资源状态")
    @GetMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestParam Long id, @RequestParam Boolean status) {
        return R.success(sysResourceService.updateStatus(id, status));
    }

    @Operation(summary = "删除资源")
    @DeleteMapping("/removeResource")
    public R<Boolean> removeResource(@RequestParam Long id) {
        return R.success(sysResourceService.removeResource(id));
    }
}
