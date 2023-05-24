package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.retrofit.APIBanThuCung;
import com.example.myapplication.retrofit.APIBanThuCungSpringBoot;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.retrofit.RetrofitSpringBoot;
import com.example.myapplication.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText email, name, password, repassword, phone;
    AppCompatButton btn_register;
    APIBanThuCung apiBanThuCung;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;
    RetrofitSpringBoot retrofitSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initControll();
    }

    private void initControll() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String str_email = email.getText().toString().trim();
        String str_name = name.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        String str_repassword = repassword.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_name)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Họ tên", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_password)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repassword)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Repassword", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_phone)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Số điện thoại", Toast.LENGTH_SHORT).show();
        } else {
            if (str_password.equals(str_repassword)) {
                // Post data
//                compositeDisposable.add(apiBanThuCung.register(str_email, str_password, str_name, str_phone)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                userModel -> {
//                                    if (userModel.isSuccess()) {
//                                        Utils.user_current.setEmail(str_email);
//                                        Utils.user_current.setPassword(str_password);
//                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
//                                    }
//                                },
//                                throwable -> {
//                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                        ));
                apiBanThuCungSpringBoot.register(str_email, str_password, str_name, str_phone).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.body().isSuccess()) {
                            Utils.user_current.setEmail(str_email);
                            Utils.user_current.setPassword(str_password);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("logggg", t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu nhập chưa khớp", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);
        email = findViewById(R.id.email_register);
        name = findViewById(R.id.name_register);
        password = findViewById(R.id.password_register);
        repassword = findViewById(R.id.repassword_register);
        phone = findViewById(R.id.phone_register);
        btn_register = findViewById(R.id.btn_register);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}