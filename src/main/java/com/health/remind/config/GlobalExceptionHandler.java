package com.health.remind.config;

import com.health.remind.config.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QQQtx
 * @since 2024/12/12 9:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验异常", ex);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return R.failed(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<String> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("异常信息:", e);
        return R.failed(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public R<String> nullException(NullPointerException e) {
        log.error("空指针异常", e);
        return R.failed("空指针异常");
    }

    @ExceptionHandler(DataException.class)
    public R<String> dataException(DataException e) {
        log.warn("业务异常:{}", e.getMessage());
        String msg = !e.getMsg()
                .isEmpty() ? ":" + e.getMsg() : "";
        return R.failed(e.getMessage() + msg);
    }

    @ExceptionHandler(Exception.class)
    public R<String> exception(Exception e) {
        log.error("系统异常", e);
        return R.failed(e.getMessage());
    }
}
