package com.health.remind.config.exception;

import com.health.remind.config.enums.DataEnums;
import lombok.Getter;

/**
 * @author QQQtx
 * @since 2024/11/26 12:41
 */
@Getter
public class DataException extends RuntimeException {

    private final int code;

    private final DataEnums dataEnums;

    private final String msg;

    public DataException(DataEnums dataEnums) {
        super(dataEnums.toString());
        this.code = dataEnums.getCode();
        this.dataEnums = dataEnums;
        this.msg = "";
    }

    public DataException(DataEnums dataEnums,String msg) {
        super(dataEnums.toString());
        this.code = dataEnums.getCode();
        this.dataEnums = dataEnums;
        this.msg = msg;
    }

    public DataException(String msg, int code, String msg1) {
        super(msg);
        this.code = code;
        this.msg = "";
        this.dataEnums = null;
    }

    @Override
    public String toString() {
        return "DataException{" + "code=" + code + ", msg='" + super.getMessage() + '}';
    }
}