package com.health.remind.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.R;
import com.health.remind.entity.Test;
import com.health.remind.pojo.dto.TestDTO;
import com.health.remind.pojo.dto.TestEntityDTO;
import com.health.remind.scheduler.DelayScheduledExecutor;
import com.health.remind.service.TestService;
import com.health.remind.util.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测试表 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
@Tag(name = "测试")
@RestController
@RequestMapping("/remind/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public R<Page<Test>> page(@RequestBody TestDTO dto) {
        return R.success(testService.pageTest(dto));
    }

    @Operation(summary = "新增或修改")
    @PostMapping("/saveOrUpdate")
    public R<Boolean> saveOrUpdate(@RequestBody TestEntityDTO dto) {
        return R.success(testService.saveOrUpdateTest(dto));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/remove")
    public R<Boolean> remove(@RequestParam Long id) {
        return R.success(testService.removeById(id));
    }

    @Operation(summary = "测试redis")
    @GetMapping("/redis")
    public R<Test> testRedis(@RequestParam Long id) {
        RedisUtils.setObject("TestController:test:testRedis", testService.getById(id));
        Test object = RedisUtils.getObject("TestController:test:testRedis", Test.class);
        return R.success(object);
    }

    @Operation(summary = "测试任务")
    @GetMapping("/task")
    public R<String> testTask() {
        DelayScheduledExecutor.putTestTask(IdWorker.getId(), CommonMethod.getMap());
        return R.success("ok");
    }
}
