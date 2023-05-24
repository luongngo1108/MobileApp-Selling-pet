package com.example.myapplication.retrofit;

import com.example.myapplication.utils.Utils;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSpringBoot {
    private Retrofit retrofit;
    
    public RetrofitSpringBoot() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL_SPRINGBOOT)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
