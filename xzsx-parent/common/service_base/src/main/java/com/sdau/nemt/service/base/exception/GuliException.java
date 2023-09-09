package com.sdau.nemt.service.base.exception;

import com.sdau.nemt.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @Date: 2022/7/28 16:45
 * @Author: 王辉
 * @Description:
 */
@Data
public class GuliException extends RuntimeException{

    private Integer code;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
