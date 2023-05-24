package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.retrofit.APIBanThuCung;
import com.example.myapplication.retrofit.APIBanThuCungSpringBoot;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.retrofit.RetrofitSpringBoot;
import com.example.myapplication.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView txtRegister, txtForgotPassword;
    EditText email, password;
    AppCompatButton btnLogin;
    APIBanThuCung apiBanThuCung;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;
    RetrofitSpringBoot retrofitSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initControll();
    }

    private void initControll() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_password = password.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
                } else {
                    // save data
                    Paper.book().write("email", str_email);
                    Paper.book().write("password", str_password);

                    login(str_email, str_password);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);

        // read data
        if (Paper.book().read("email") != null && Paper.book().read("password") != null) {
            email.setText(Paper.book().read("email"));
            password.setText(Paper.book().read("password"));
            if (Paper.book().read("isLogin") != null) {
                boolean flag = Paper.book().read("isLogin");
                if (flag) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //login(Paper.book().read("email"), Paper.book().read("password"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void login(String email, String password) {
//        compositeDisposable.add(apiBanThuCung.login(email, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        userModel -> {
//                            if (userModel.isSuccess()) {
//                                isLogin = true;
//                                Paper.book().write("isLogin", isLogin);
//                                Utils.user_current = userModel.getResult().get(0);
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                ));

        apiBanThuCungSpringBoot.login(email, password).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.body().isSuccess()) {
                    isLogin = true;
                    Paper.book().write("isLogin", isLogin);
                    Utils.user_current = response.body().getResult().get(0);
                    // lưu thông tin người dùng
                    Paper.book().write("user", response.body().getResult().get(0));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email hoặc Password chưa chính xác", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null) {
            email.setText(Utils.user_current.getEmail());
            password.setText(Utils.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}