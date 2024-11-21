package com.group4.meetingroom.entity.vo;

import com.group4.meetingroom.entity.User;
import org.springframework.http.HttpStatus;

public class MessageModel <T>{
    private Integer status = 0;//状态码
    private String msg = "失败";
    private T data;

    public MessageModel() {
    }

    public  MessageModel (Integer status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}


