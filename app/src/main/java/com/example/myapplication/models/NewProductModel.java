package com.example.myapplication.models;

import java.util.List;

public class NewProductModel {
    boolean success;
    String message;
    List<NewProduct> result;

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

    public List<NewProduct> getResult() {
        return result;
    }

    public void setResult(List<NewProduct> result) {
        this.result = result;
    }
}
