package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.common.enums.FrequencyTypeEnum;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.FrequencyDTO;
import com.health.remind.pojo.dto.FrequencyPageDTO;
import com.health.remind.pojo.vo.FrequencyVO;
import com.health.remind.service.FrequencyService;
import com.health.remind.service.impl.FrequencyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 频率 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Tag(name = "频率")
@RestController
@RequestMapping("/remind/frequencyRule")
public class FrequencyController {

    private final FrequencyService frequencyService;

    public FrequencyController(FrequencyServiceImpl frequencyService) {
        this.frequencyService = frequencyService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageFrequency")
    public R<Page<FrequencyVO>> pageFrequency(@RequestBody FrequencyPageDTO dto) {
        return R.success(frequencyService.pageFrequency(dto));
    }

    @Operation(summary = "查询所有")
    @GetMapping("/listFrequency")
    public R<List<FrequencyVO>> listFrequency() {
        return R.success(frequencyService.listFrequency());
    }

    @Operation(summary = "查询详情")
    @GetMapping("/getFrequency")
    public R<FrequencyVO> getFrequency(@RequestParam Long id) {
        return R.success(frequencyService.getFrequency(id));
    }

    @Operation(summary = "新增或修改")
    @PostMapping("/saveOrUpdateFrequency")
    public R<Boolean> saveOrUpdateFrequency(@RequestBody @Valid FrequencyDTO frequencyVO) {
        return R.success(frequencyService.saveOrUpdateFrequency(frequencyVO));
    }

    @Operation(summary = "修改状态")
    @GetMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestParam Long id, @RequestParam boolean status) {
        return R.success(frequencyService.updateStatus(id, status));
    }

    @Operation(summary = "修改类型")
    @GetMapping("/updateType")
    public R<Boolean> updateType(@RequestParam Long id, @RequestParam FrequencyTypeEnum type) {
        return R.success(frequencyService.updateType(id, type));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/removeFrequency")
    public R<Boolean> removeFrequency(@RequestParam Long id) {
        return R.success(frequencyService.removeFrequency(id));
    }
}
