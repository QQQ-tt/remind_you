package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.pojo.dto.RuleTemplatePageDTO;
import com.health.remind.service.RuleTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 规则模板 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Tag(name = "规则模板")
@RestController
@RequestMapping("/remind/ruleTemplate")
public class RuleTemplateController {

    private final RuleTemplateService ruleTemplateService;

    public RuleTemplateController(RuleTemplateService ruleTemplateService) {
        this.ruleTemplateService = ruleTemplateService;
    }

    @Operation(summary = "分页查询规则模板")
    @PostMapping("/pageRuleTemplate")
    public R<Page<RuleTemplate>> pageRuleTemplate(@RequestBody RuleTemplatePageDTO dto) {
        return R.success(ruleTemplateService.pageRuleTemplate(dto));
    }

    @Operation(summary = "保存或更新规则模板")
    @PutMapping("/saveOrUpdateRuleTemplate")
    public R<Boolean> saveOrUpdateRuleTemplate(@RequestBody @Valid RuleTemplate ruleTemplate) {
        return R.success(ruleTemplateService.saveOrUpdateRuleTemplate(ruleTemplate));
    }

    @Operation(summary = "更新规则模板状态")
    @PutMapping("/updateRuleTemplateStatus")
    public R<Boolean> updateRuleTemplateStatus(@RequestBody RuleTemplate ruleTemplate) {
        return R.success(ruleTemplateService.updateRuleTemplateStatus(ruleTemplate.getId(), ruleTemplate.getStatus()));
    }

}
