package com.example.demo.Model;


/**
 * Created by snsoft on 19/7/2017.
 */
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
        this.code = -1;
        this.msg = "未知异常";
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
//    class Data{
//        private T data;
//        private int page;
//        private int total;
//    }
}
