package com.example.wmsspringbootproject.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private String code;
    private String  msg;
    private Object data;

    public static Result success(Object data) {
        return new Result(true,"200","success",data);
    }

    public static Result fail(String code, String msg){
        return new Result(false, code, msg, null);
    }
}
