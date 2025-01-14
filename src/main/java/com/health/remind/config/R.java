package com.health.remind.config;


import lombok.Data;
import com.health.remind.config.enums.DataEnums;


/**
 * @author qtx
 * @since 2023/7/11
 */
@Data
public class R<T> {
    private String msg;
    private int code;
    private T data;

    private R(int code) {
        this.code = code;
    }

    private R(String msg) {
        this.msg = msg;
    }

    private R(T msg) {
        this.data = msg;
    }

    private R(DataEnums enums) {
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }

    private R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private R(T data, DataEnums enums) {
        this.data = data;
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }

    private R(String msg, DataEnums enums) {
        this.msg = msg;
        this.code = enums.getCode();
    }

    private R(String msg, int code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static <T> R<T> success(T date) {
        return new R<>(date, DataEnums.SUCCESS);
    }

    public static <T> R<T> failed(T data) {
        return new R<>(data, DataEnums.FAILED);
    }

    public static <T> R<T> success() {
        return new R<>(DataEnums.SUCCESS.getMsg(), DataEnums.SUCCESS);
    }

    public static <T> R<T> failed() {
        return new R<>(DataEnums.FAILED.getMsg(), DataEnums.FAILED);
    }

    public static <T> R<T> failed(int code) {
        return new R<>(code);
    }

    public static <T> R<T> failed(String msg) {
        return new R<>(msg, DataEnums.FAILED);
    }

    public static <T> R<T> failed(String msg, int code) {
        return new R<>(code, msg);
    }

    public static <T> R<T> failed(String msg, int code, T data) {
        return new R<>(msg, code, data);
    }

    public static <T> R<T> failed(DataEnums enums) {
        return new R<>(enums);
    }

    public static <T> R<T> failed(DataEnums enums, T data) {
        return new R<>(data, enums);
    }
}
