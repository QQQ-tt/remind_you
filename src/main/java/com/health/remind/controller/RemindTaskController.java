package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskIndoDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.service.RemindTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 提醒任务 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Tag(name = "提醒任务")
@RestController
@RequestMapping("/remind/remindTask")
public class RemindTaskController {

    private final RemindTaskService remindTaskService;

    public RemindTaskController(RemindTaskService remindTaskService) {
        this.remindTaskService = remindTaskService;
    }

    @Operation(summary = "根据用户分页查询提醒任务")
    @PostMapping("/pageTaskByUserId")
    public R<Page<RemindTaskVO>> pageTaskByUserId(@RequestBody RemindTaskPageDTO dto) {
        return R.success(remindTaskService.pageTaskByUserId(dto));
    }

    @Operation(summary = "根据id查询提醒任务")
    @GetMapping("/getTaskById")
    public R<RemindTaskVO> getTaskById(@RequestParam Long id) {
        return R.success(remindTaskService.getTaskById(id));
    }

    @Operation(summary = "保存提醒任务")
    @PostMapping("/saveOrUpdateTask")
    public R<Boolean> saveOrUpdateTask(@RequestBody RemindTaskDTO task) {
        return R.success(remindTaskService.saveOrUpdateTask(task));
    }

    @Operation(summary = "测试任务详情")
    @PostMapping("/testTaskInfoNumTen")
    public R<List<LocalDateTime>> testTaskInfoNumTen(@RequestBody RemindTaskDTO task) {
        return R.success(remindTaskService.testTaskInfoNumTen(task));
    }

    @Operation(summary = "导出任务时间详情")
    @PostMapping("/exportTaskInfo")
    public void exportTaskInfo(@RequestParam Long id, HttpServletResponse response) {
        remindTaskService.exportTaskInfo(id, response);
    }

    @Operation(summary = "根据用户id查询时间范围内提醒任务详情")
    @PostMapping("/getTaskInfoByUserId")
    public R<List<RemindTaskInfoVO>> getTaskInfoByUserId(@RequestBody RemindTaskIndoDTO dto) {
        return R.success(remindTaskService.getTaskInfoByUserId(dto));
    }

    @Operation(summary = "删除提醒任务")
    @DeleteMapping("/removeTask")
    public R<Boolean> removeTask(@RequestParam Long id) {
        return R.success(remindTaskService.removeTask(id));
    }
}
