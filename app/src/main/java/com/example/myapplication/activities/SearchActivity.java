package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ItemCategoryAdapter;
import com.example.myapplication.models.NewProduct;
import com.example.myapplication.models.NewProductModel;
import com.example.myapplication.retrofit.APIBanThuCung;
import com.example.myapplication.retrofit.APIBanThuCungSpringBoot;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.retrofit.RetrofitSpringBoot;
import com.example.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rc_search;
    EditText edt_search;
    ItemCategoryAdapter itemCategoryAdapter;
    List<NewProduct> newProductList;
    APIBanThuCung apiBanThuCung;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RetrofitSpringBoot retrofitSpringBoot;
    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        ActionToolBar();
    }

    private void initView() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);

        newProductList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_search);
        rc_search = findViewById(R.id.rc_search);
        edt_search = findViewById(R.id.edt_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rc_search.setHasFixedSize(true);
        rc_search.setLayoutManager(layoutManager);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    newProductList.clear();
                    itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), newProductList);
                    rc_search.setAdapter(itemCategoryAdapter);
                } else {
                    getDataSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getDataSearch(String s) {
        newProductList.clear();
//        compositeDisposable.add(apiBanThuCung.search(s)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        newProductModel -> {
//                            if (newProductModel.isSuccess()) {
//                                newProductList = newProductModel.getResult();
//                                itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), newProductList);
//                                rc_search.setAdapter(itemCategoryAdapter);
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                ));

        apiBanThuCungSpringBoot.search(s).enqueue(new Callback<NewProductModel>() {
            @Override
            public void onResponse(Call<NewProductModel> call, Response<NewProductModel> response) {
                if (response.body().isSuccess()) {
                    newProductList = response.body().getResult();
                    itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), newProductList);
                    rc_search.setAdapter(itemCategoryAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi!!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewProductModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}