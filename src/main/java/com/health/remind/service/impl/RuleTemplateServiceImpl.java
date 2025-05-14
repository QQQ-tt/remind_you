package com.health.remind.service.impl;

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
    public Page<RuleTemplate> ruleTemplatePage(RuleTemplatePageDTO dto) {
        return page(dto.getPage(), Wrappers.lambdaQuery(RuleTemplate.class)
                .orderByDesc(BaseEntity::getCreateTime));
    }

    @Override
    public boolean saveOrUpdateRuleTemplate(RuleTemplate ruleTemplate) {
        return saveOrUpdate(ruleTemplate);
    }

    @Override
    public boolean updateRuleTemplateStatus(Long id, Boolean status) {
        return update(Wrappers.lambdaUpdate(RuleTemplate.class)
                .eq(BaseEntity::getId, id)
                .set(RuleTemplate::getStatus, status));
    }
}
