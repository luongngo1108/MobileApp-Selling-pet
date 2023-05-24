package com.example.myapplication.models;

import java.util.List;

public class CategoriesModel {
    boolean success;
    String message;
    List<Categories> result;

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

    public List<Categories> getResult() {
        return result;
    }

    public void setResult(List<Categories> result) {
        this.result = result;
    }
}
