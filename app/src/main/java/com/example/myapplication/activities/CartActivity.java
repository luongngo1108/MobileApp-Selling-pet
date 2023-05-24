package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.models.EventBus.TotalCountEvent;
import com.example.myapplication.models.ItemCart;
import com.example.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView cartEmpty, totalBill;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnBuy;
    CartAdapter cartAdapter;
    long totalCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        initControl();
        totalCount();
    }

    private void totalCount() {
        totalCart = 0;
        for (int i = 0; i < Utils.listCartBuy.size(); i++) {
            totalCart = totalCart + Long.parseLong(Utils.listCartBuy.get(i).getPrice()) * Utils.listCartBuy.get(i).getQuantity();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalBill.setText(decimalFormat.format(Double.parseDouble(String.valueOf(totalCart))));
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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.listCart.size() == 0) {
            cartEmpty.setVisibility(View.VISIBLE);
        } else {
            cartAdapter = new CartAdapter(getApplicationContext(), Utils.listCart);
            recyclerView.setAdapter(cartAdapter);
        }

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("totalCart", totalCart);
                Utils.listCart.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        cartEmpty = findViewById(R.id.txtCartEmpty);
        totalBill = findViewById(R.id.txtTotalBill);
        toolbar = findViewById(R.id.toolbar_cart);
        recyclerView = findViewById(R.id.rc_cart);
        btnBuy = findViewById(R.id.btn_buy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTotalCount(TotalCountEvent event) {
        if (event != null) {
            totalCount();
        }
    }
}