package com.example.myapplication.utils;

import com.example.myapplication.models.ItemCart;
import com.example.myapplication.models.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.183.1/doan_mobile/";
    public static final String BASE_URL_SPRINGBOOT = "http://192.168.183.1:9000/api/banthucung/";
    public static List<ItemCart> listCart;
    public static List<ItemCart> listCartBuy = new ArrayList<>();
    public static User user_current = new User();
}
