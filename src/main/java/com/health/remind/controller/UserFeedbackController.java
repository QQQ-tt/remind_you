package com.health.remind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.remind.config.R;
import com.health.remind.entity.UserFeedback;
import com.health.remind.pojo.dto.UserFeedbackDTO;
import com.health.remind.pojo.dto.UserFeedbackPageDTO;
import com.health.remind.service.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 意见反馈表 前端控制器
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-07
 */
@Tag(name = "意见反馈表")
@RestController
@RequestMapping("/remind/userFeedback")
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;

    public UserFeedbackController(UserFeedbackService userFeedbackService) {
        this.userFeedbackService = userFeedbackService;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pageUserFeedback")
    public R<Page<UserFeedback>> pageUserFeedback(@RequestBody @Valid UserFeedbackPageDTO dto) {
        return R.success(userFeedbackService.pageUserFeedback(dto));
    }

    @Operation(summary = "通过用户查询")
    @PostMapping("/listUserFeedbackByUser")
    public R<List<UserFeedback>> listUserFeedbackByUser() {
        return R.success(userFeedbackService.listUserFeedbackByUser());
    }

    @Operation(summary = "保存意见反馈")
    @PostMapping("/saveOrUpdate")
    public R<Boolean> saveOrUpdate(@RequestBody @Valid UserFeedback dto) {
        return R.success(userFeedbackService.saveUserFeedback(dto));
    }

    @Operation(summary = "处理意见")
    @PostMapping("/handlingComments")
    public R<Boolean> handlingComments(@RequestBody @Valid UserFeedbackDTO dto) {
        return R.success(userFeedbackService.handlingComments(dto));
    }
}
