package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView totalBill, phoneNumber, email;
    EditText address;
    AppCompatButton btn_payment;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanThuCung apiBanThuCung;
    long totalCart;
    int totalItem;
    RetrofitSpringBoot retrofitSpringBoot;
    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for(int i = 0; i < Utils.listCartBuy.size(); i++) {
            totalItem = totalItem + Utils.listCartBuy.get(i).getQuantity();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalCart = getIntent().getLongExtra("totalCart", 0);
        totalBill.setText(decimalFormat.format(totalCart));
        email.setText(Utils.user_current.getEmail());
        phoneNumber.setText(Utils.user_current.getPhone());

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_address = address.getText().toString().trim();
                if (TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                } else {
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_phone = Utils.user_current.getPhone();
                    int id = Utils.user_current.getId();
                    Log.d("logggg", new Gson().toJson(Utils.listCartBuy));
//                    compositeDisposable.add(apiBanThuCung.createOrders(str_email, str_phone, String.valueOf(totalCart), id, str_address, totalItem, new Gson().toJson(Utils.listCartBuy))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(
//                                    userModel -> {
//                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_LONG).show();
//                                        Utils.listCartBuy.clear();
//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    },
//                                    throwable -> {
//                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//                                    }
//                            ));

                    apiBanThuCungSpringBoot.createOrders(str_email, str_phone, String.valueOf(totalCart), id, str_address, totalItem, new Gson().toJson(Utils.listCartBuy)).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_LONG).show();
                            Utils.listCartBuy.clear();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);

        toolbar = findViewById(R.id.toolbar_payment);
        totalBill = findViewById(R.id.totalBill_payment);
        phoneNumber = findViewById(R.id.phoneNumber_payment);
        email = findViewById(R.id.email_payment);
        address = findViewById(R.id.address_payment);
        btn_payment = findViewById(R.id.btn_payment);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}