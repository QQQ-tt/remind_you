package com.health.remind.config.exception;

import com.health.remind.config.R;
import com.health.remind.scheduler.LogConsumerExecutor;
import com.health.remind.scheduler.entity.ExceptionTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QQQtx
 * @since 2024/12/12 9:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LogConsumerExecutor exceptionConsumerExecutor;

    public GlobalExceptionHandler(LogConsumerExecutor exceptionConsumerExecutor) {
        this.exceptionConsumerExecutor = exceptionConsumerExecutor;
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<R<String>> authException(AuthException e) {
        log.warn("认证异常:{}", e.getMessage());
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(2)
                .message(e.getMessage())
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<R<String>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("异常信息:", e);
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(1)
                .message(e.getMessage())
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<R<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HTTP消息读取异常: {}", e.getMessage());
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(1)
                .message(e.getMessage())
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed("请求参数格式错误: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验异常", ex);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(ex.getClass()
                        .getName())
                .level(1)
                .message(errors.toString())
                .stackTrace(Arrays.toString(ex.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.verifyFailed(errors), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<R<String>> nullException(NullPointerException e) {
        log.error("空指针异常", e);
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(2)
                .message(e.getMessage())
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed("空指针异常"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<R<String>> dataException(DataException e) {
        log.warn("业务异常:{}", e.getMessage());
        String msg = !e.getMsg()
                .isEmpty() ? ":" + e.getMsg() : "";
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(2)
                .message(e.getMessage() + msg)
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed(e.getMessage() + msg, e.getCode()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<String>> exception(Exception e) {
        log.error("系统异常", e);
        exceptionConsumerExecutor.putExceptionTask(ExceptionTask.builder()
                .exceptionName(e.getClass()
                        .getName())
                .level(3)
                .message(e.getMessage())
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
        return new ResponseEntity<>(R.failed(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
