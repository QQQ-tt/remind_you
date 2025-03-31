package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.pojo.dto.FrequencyDetailDTO;
import com.health.remind.pojo.dto.FrequencyDetailPageDTO;
import com.health.remind.pojo.vo.FrequencyDetailVO;
import com.health.remind.service.FrequencyDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 频次详情表(时间明细表) 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Tag(name = "频率详情")
@RestController
@RequestMapping("/remind/frequencyDetail")
public class FrequencyDetailController {

    private final FrequencyDetailService frequencyDetailService;

    public FrequencyDetailController(FrequencyDetailService frequencyDetailService) {
        this.frequencyDetailService = frequencyDetailService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageFrequencyDetail")
    public R<Page<FrequencyDetailVO>> pageFrequencyDetail(@RequestBody @Valid FrequencyDetailPageDTO dto) {
        return R.success(frequencyDetailService.pageFrequencyDetail(dto));
    }

    @Operation(summary = "新增或修改")
    @PostMapping("/saveOrUpdateFrequencyDetail")
    public R<Boolean> saveOrUpdateFrequencyDetail(@RequestBody @Valid FrequencyDetailDTO dto) {
        return R.success(frequencyDetailService.saveOrUpdateFrequencyDetail(dto));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/removeFrequencyDetailById")
    public R<Boolean> removeFrequencyDetailById(@RequestParam Long id) {
        return R.success(frequencyDetailService.removeFrequencyDetailById(id));
    }
}
