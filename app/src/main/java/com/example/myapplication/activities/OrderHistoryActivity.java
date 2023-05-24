package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrdersAdapter;
import com.example.myapplication.retrofit.APIBanThuCung;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderHistoryActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanThuCung apiBanThuCung;
    RecyclerView rcOrders;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initView();
        initToolBar();
        getOrders();
    }

    private void getOrders() {
        compositeDisposable.add(apiBanThuCung.ordersHistory(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ordersModel -> {
                            OrdersAdapter adapter = new OrdersAdapter(getApplicationContext(), ordersModel.getResult());
                            rcOrders.setAdapter(adapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);
        toolbar = findViewById(R.id.toolbar_orderHistory);
        rcOrders = findViewById(R.id.rc_orderHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcOrders.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}