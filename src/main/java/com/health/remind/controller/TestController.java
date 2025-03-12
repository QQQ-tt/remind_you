package com.health.remind.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.common.StaticConstant;
import com.health.remind.config.CommonMethod;
import com.health.remind.config.R;
import com.health.remind.entity.Test;
import com.health.remind.pojo.dto.TestDTO;
import com.health.remind.pojo.dto.TestEntityDTO;
import com.health.remind.scheduler.DelayAttenuationScheduledExecutor;
import com.health.remind.scheduler.DelayScheduledExecutor;
import com.health.remind.scheduler.ScheduledBase;
import com.health.remind.scheduler.enums.ScheduledEnum;
import com.health.remind.service.TestService;
import com.health.remind.util.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * <p>
 * 测试表 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-14
 */
@Slf4j
@Tag(name = "测试", description = StaticConstant.PERMISSION_KEY)
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

    @Operation(summary = "测试衰减轮询任务")
    @GetMapping("/testDelayAttenuationTask")
    public R<Long> testDelayAttenuationScheduledTask() {
        long id = IdWorker.getId();
        DelayAttenuationScheduledExecutor.putTestTask(id, CommonMethod.getMap());
        return R.success(id);
    }

    @Operation(summary = "测试延时任务")
    @GetMapping("/testDelayTask")
    public R<Long> testDelayScheduledTask(@RequestParam(required = false) Integer num) {
        long id = IdWorker.getId();
        int randomNum = (int) (Math.random() * 10) + 1;
        if (num != null) {
            randomNum = num;
        }
        DelayScheduledExecutor.putTestTask(id, LocalDateTime.now()
                .plusMinutes(randomNum), CommonMethod.getMap());
        return R.success(id);
    }

    @Operation(summary = "取消任务")
    @GetMapping("/cancelTask")
    public R<String> cancelTask(@RequestParam(required = false) Long taskId, @RequestParam ScheduledEnum scheduledEnum) {
        ScheduledBase.cancelTask(taskId, scheduledEnum);
        return R.success("ok");
    }

    @Operation(summary = "判断任务是否存在")
    @GetMapping("/containsTask")
    public R<Boolean> containsTask(@RequestParam Long taskId, @RequestParam ScheduledEnum scheduledEnum) {
        return R.success(ScheduledBase.containsTask(taskId, scheduledEnum));
    }

    @Operation(summary = "获取等待任务数量")
    @GetMapping("/getTaskSize")
    public R<Integer> getTaskSize(@RequestParam ScheduledEnum scheduledEnum) {
        return R.success(ScheduledBase.getTaskSize(scheduledEnum));
    }

    @Operation(summary = "获取安排任务数量")
    @GetMapping("/getFutureSize")
    public R<Integer> getFutureSize(@RequestParam ScheduledEnum scheduledEnum) {
        return R.success(ScheduledBase.getFutureSize(scheduledEnum));
    }

    @Operation(summary = "测试redis锁")
    @GetMapping("/testRedisLock")
    public R<String> testRedisLock(@RequestParam String key, @RequestParam Long length) {
        return R.success(testService.testRedisLock(key, length));
    }

    @Operation(summary = "测试异常日志")
    @PostMapping("/testExceptionLog")
    public R<Integer> testExceptionLog(@RequestBody @Valid TestEntityDTO dto) {
        int i = (dto.getNum() - 50) / 2;
        if (i == 0) {
            throw new NullPointerException("测试空指针异常日志");
        }
        return R.success(i);
    }

    /**
     * string.format 用法
     * %s：字符串
     * %d：十进制整数
     * %f：浮点数
     * %t：日期/时间
     * %n：换行符
     */
    @Operation(summary = "测试token校验", description = StaticConstant.PERMISSION_KEY)
    @GetMapping("/testToken/{key}/acc/{val}")
    public R<String> testToken(@PathVariable String key, @PathVariable String val) {
        return R.success(String.format("测试token校验成功:%s,%s", key, val));
    }

    @Operation(summary = "生成随机数", description = StaticConstant.PERMISSION_KEY)
    @GetMapping("/randomNum")
    public R<String> randomNum() {
        Random random = new Random();
        // 生成6位随机数 从 0 到 899999 之间的随机整数
        int number = random.nextInt(900000) + 100000;
        return R.success(String.valueOf(number));
    }

    @Operation(summary = "测试cache")
    @GetMapping("/testCache")
    public R<String> testCache(@RequestParam String value) {
        return R.success(testService.testCache(value));
    }

    @Operation(summary = "测试cache")
    @GetMapping("/testCache2")
    public R<String> testCache2(@RequestParam String value) {
        return R.success(testService.testCache2(value));
    }

    @Operation(summary = "测试cache")
    @GetMapping("/testCache3")
    public R<String> testCache3(@RequestParam String value) {
        return R.success(testService.testCache3(value));
    }

    @Operation(summary = "测试cache")
    @GetMapping("/testCache4")
    public R<String> testCache4(@RequestParam String value) {
        return R.success(testService.testCache4(value));
    }
}
