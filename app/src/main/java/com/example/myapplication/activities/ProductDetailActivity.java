package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.ItemCart;
import com.example.myapplication.models.NewProduct;
import com.example.myapplication.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {
    TextView name, price, description;
    Button btn_Add;
    ImageView image;
    Spinner spinner;
    Toolbar toolbar;
    NewProduct productDetail;
    NotificationBadge notificationBadge;
    FrameLayout frameCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        if(Utils.listCart.size() > 0) {
            boolean flag = false;
            int quantity = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.listCart.size(); i++) {
                if(Utils.listCart.get(i).getProduct_id() == productDetail.getId()) {
                    Utils.listCart.get(i).setQuantity(quantity + Utils.listCart.get(i).getQuantity());
                    long total = Long.parseLong(productDetail.getPrice()) * Utils.listCart.get(i).getQuantity();
                    Utils.listCart.get(i).setTotal(total);
                    flag = true;
                }
            }
            if(flag == false) {
                long total = Long.parseLong(productDetail.getPrice()) * quantity;
                ItemCart itemCart = new ItemCart();
                itemCart.setName(productDetail.getName());
                itemCart.setProduct_id(productDetail.getId());
                itemCart.setPrice(productDetail.getPrice());
                itemCart.setImage(productDetail.getImage());
                itemCart.setQuantity(quantity);
                itemCart.setTotal(total);
                Utils.listCart.add(itemCart);
            }
        } else {
            int quantity = Integer.parseInt(spinner.getSelectedItem().toString());
            long total = Long.parseLong(productDetail.getPrice()) * quantity;
            ItemCart itemCart = new ItemCart();
            itemCart.setName(productDetail.getName());
            itemCart.setProduct_id(productDetail.getId());
            itemCart.setPrice(productDetail.getPrice());
            itemCart.setImage(productDetail.getImage());
            itemCart.setQuantity(quantity);
            itemCart.setTotal(total);
            Utils.listCart.add(itemCart);
        }
        int totalItem = 0;
        for(int i = 0; i < Utils.listCart.size(); i++) {
            totalItem = totalItem + Utils.listCart.get(i).getQuantity();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        productDetail = (NewProduct) getIntent().getSerializableExtra("detail");
        name.setText(productDetail.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        price.setText("Giá: " + decimalFormat.format(Double.parseDouble(productDetail.getPrice())) + "VND");
        description.setText(productDetail.getDescription());
        Glide.with(getApplicationContext()).load(productDetail.getImage()).into(image);
        Integer[] numbers = new Integer[] {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, numbers);
        spinner.setAdapter(adapterSpinner);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.listCart != null) {
            int totalItem = 0;
            for(int i = 0; i < Utils.listCart.size(); i++) {
                totalItem = totalItem + Utils.listCart.get(i).getQuantity();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }

    private void initView() {
        name = findViewById(R.id.txtProductDetail_name);
        price = findViewById(R.id.txtProductDetail_price);
        description = findViewById(R.id.txtProductDetail_description);
        btn_Add = findViewById(R.id.btn_addCart);
        spinner = findViewById(R.id.sniper_productDetail);
        image = findViewById(R.id.image_productDetail);
        toolbar = findViewById(R.id.toolbar_productDetail);
        notificationBadge = findViewById(R.id.menuNotify);
        frameCart = findViewById(R.id.frame_cart);
        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        if(Utils.listCart != null) {
            int totalItem = 0;
            for(int i = 0; i < Utils.listCart.size(); i++) {
                totalItem = totalItem + Utils.listCart.get(i).getQuantity();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }
}