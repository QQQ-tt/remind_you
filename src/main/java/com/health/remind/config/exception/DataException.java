package com.health.remind.config.exception;

import com.health.remind.config.enums.DataEnums;
import lombok.Getter;

/**
 * @author qtx
 * @since 2024/11/26 12:41
 */
@Getter
public class DataException extends RuntimeException {

    private final int code;

    private final DataEnums dataEnums;

    public DataException(DataEnums dataEnums) {
        super(dataEnums.toString());
        this.code = dataEnums.getCode();
        this.dataEnums = dataEnums;
    }

    public DataException(String msg, int code) {
        super(msg);
        this.code = code;
        this.dataEnums = null;
    }

    @Override
    public String toString() {
        return "DataException{" + "code=" + code + ", msg='" + super.getMessage() + '}';
    }
}