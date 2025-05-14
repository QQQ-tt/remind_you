package com.health.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.entity.RuleTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.remind.pojo.dto.RuleTemplatePageDTO;


/**
 * <p>
 * 规则模板 服务类
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
public interface RuleTemplateService extends IService<RuleTemplate> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return Page<RuleTemplate>
     */
    Page<RuleTemplate> ruleTemplatePage(RuleTemplatePageDTO dto);

    /**
     * 保存或更新
     *
     * @param ruleTemplate ruleTemplate
     * @return boolean
     */
    boolean saveOrUpdateRuleTemplate(RuleTemplate ruleTemplate);

    /**
     * 更新状态
     *
     * @param id     模板id
     * @param status 状态
     * @return boolean
     */
    boolean updateRuleTemplateStatus(Long id, Boolean status);
}
