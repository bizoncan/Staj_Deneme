package com.example.staj_deneme.Models;

public class ResponseModel {
    public Boolean success;
    public String message;
    public Integer userId;
    public Boolean isSuccess(){
        return  success;
    }
    public String getMessage(){
        return message;
    }
    public Integer getUserId() {
        return userId;
    }
}
