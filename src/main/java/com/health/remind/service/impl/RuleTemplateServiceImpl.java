package com.health.remind.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.remind.config.BaseEntity;
import com.health.remind.entity.RuleTemplate;
import com.health.remind.mapper.RuleTemplateMapper;
import com.health.remind.pojo.dto.RuleTemplatePageDTO;
import com.health.remind.service.RuleTemplateService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 规则模板 服务实现类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Service
public class RuleTemplateServiceImpl extends ServiceImpl<RuleTemplateMapper, RuleTemplate> implements RuleTemplateService {

    @Override
    public Page<RuleTemplate> pageRuleTemplate(RuleTemplatePageDTO dto) {
        return page(dto.getPage(), Wrappers.lambdaQuery(RuleTemplate.class)
                .eq(dto.getStatus() != null, RuleTemplate::getStatus, dto.getStatus())
                .like(StringUtils.isNotBlank(dto.getName()), RuleTemplate::getName, dto.getName())
                .eq(dto.getInterestsLevel() != null, RuleTemplate::getInterestsLevel, dto.getInterestsLevel())
                .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public boolean saveOrUpdateRuleTemplate(RuleTemplate ruleTemplate) {
        return saveOrUpdate(ruleTemplate);
    }

    @Override
    public boolean updateRuleTemplateStatus(Long id, Boolean status) {
        RuleTemplate ruleTemplate = new RuleTemplate();
        ruleTemplate.setId(id);
        ruleTemplate.setStatus(status);
        return updateById(ruleTemplate);
    }
}
