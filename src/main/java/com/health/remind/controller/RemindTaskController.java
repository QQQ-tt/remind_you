package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.RemindTaskDTO;
import com.health.remind.pojo.dto.RemindTaskDelDTO;
import com.health.remind.pojo.dto.RemindTaskIndoDTO;
import com.health.remind.pojo.dto.RemindTaskPageDTO;
import com.health.remind.pojo.vo.RemindTaskInfoVO;
import com.health.remind.pojo.vo.RemindTaskVO;
import com.health.remind.service.RemindTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "分页查询提醒任务")
    @PostMapping("/pageTask")
    public R<Page<RemindTaskVO>> pageTask(@RequestBody RemindTaskPageDTO dto) {
        return R.success(remindTaskService.pageTask(dto));
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
    public R<Boolean> saveOrUpdateTask(@RequestBody @Valid RemindTaskDTO task) {
        return R.success(remindTaskService.saveOrUpdateTask(task));
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public R<Boolean> sendEmailCode(@RequestBody @Valid RemindTaskDTO task) {
        remindTaskService.sendEmailCode(task.getEmail());
        return R.success();
    }

    @Operation(summary = "重置提醒任务（小时手动触发型任务）")
    @GetMapping("/resetTask")
    public R<Boolean> resetTask(@RequestParam Long id) {
        return R.success(remindTaskService.resetTask(id));
    }

    @Operation(summary = "修改提醒任务状态")
    @GetMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestParam Long id) {
        return R.success(remindTaskService.updateStatus(id));
    }

    @Operation(summary = "测试任务详情")
    @PostMapping("/testTaskInfoNumTen")
    public R<List<LocalDateTime>> testTaskInfoNumTen(@RequestBody RemindTaskDTO task) {
        return R.success(remindTaskService.testTaskInfoNumTen(task));
    }

    @Operation(summary = "导出任务时间详情")
    @GetMapping("/exportTaskInfo")
    public void exportTaskInfo(@RequestParam Long id, HttpServletResponse response) {
        remindTaskService.exportTaskInfo(id, response);
    }

    @Operation(summary = "根据用户id查询时间范围内提醒任务详情")
    @PostMapping("/getTaskInfoByUserId")
    public R<Map<LocalDate, List<RemindTaskInfoVO>>> getTaskInfoByUserId(@RequestBody RemindTaskIndoDTO dto) {
        return R.success(remindTaskService.getTaskInfoByUserId(dto));
    }

    @Operation(summary = "删除提醒任务")
    @DeleteMapping("/removeTaskById")
    public R<Boolean> removeTaskById(@RequestBody RemindTaskDelDTO dto) {
        return R.success(remindTaskService.removeTaskById(dto.getId()));
    }
}
