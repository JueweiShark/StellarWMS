package com.example.wmsspringbootproject.core.security.exception;

import com.example.wmsspringbootproject.common.result.IResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private IResultCode iResultCode;

    public BusinessException(IResultCode iResultCode) {
        super(iResultCode.getMsg());
        this.iResultCode = iResultCode;
    }

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }

    public BusinessException(Throwable cause){
        super(cause);
    }
}
