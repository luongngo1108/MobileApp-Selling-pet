package com.example.myapplication.models;

import java.util.List;

public class OrdersModel {
    boolean success;
    String message;
    List<Orders> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Orders> getResult() {
        return result;
    }

    public void setResult(List<Orders> result) {
        this.result = result;
    }
}
