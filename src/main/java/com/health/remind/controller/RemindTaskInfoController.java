package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.RemindTaskInfoDTO;
import com.health.remind.pojo.dto.RemindTaskInfoPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.service.RemindTaskInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 任务执行详情数据 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-27
 */
@Tag(name = "提醒任务详情")
@RestController
@RequestMapping("/remind/remindTaskInfo")
public class RemindTaskInfoController {

    private final RemindTaskInfoService remindTaskInfoService;

    public RemindTaskInfoController(RemindTaskInfoService remindTaskInfoService) {
        this.remindTaskInfoService = remindTaskInfoService;
    }

    @Operation(summary = "分页查询任务详情")
    @PostMapping("/pageTaskInfo")
    public R<Page<RemindTaskInfoVO>> pageTaskInfo(@RequestBody @Valid RemindTaskInfoPageDTO dto) {
        return R.success(remindTaskInfoService.pageTaskInfo(dto));
    }

    @Operation(summary = "查询任务详情")
    @PostMapping("/listTaskInfo")
    public R<List<RemindTaskInfoVO>> listTaskInfo(@RequestBody @Valid RemindTaskInfoDTO dto) {
        return R.success(remindTaskInfoService.listTaskInfo(dto));
    }
}
