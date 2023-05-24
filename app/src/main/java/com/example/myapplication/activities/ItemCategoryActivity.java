package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ItemCategoryAdapter;
import com.example.myapplication.models.NewProduct;
import com.example.myapplication.models.Products;
import com.example.myapplication.models.ProductsModel;
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

public class ItemCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    APIBanThuCung apiBanThuCung;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int type;
    String typeName;
    ItemCategoryAdapter itemCategoryAdapter;
    List<NewProduct> newProductList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    RetrofitSpringBoot retrofitSpringBoot;
    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category);

        type = getIntent().getIntExtra("type",1 );
        typeName = getIntent().getStringExtra("typeName");

        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoading();
    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false) {
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == newProductList.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                newProductList.add(null);
                itemCategoryAdapter.notifyItemInserted(newProductList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newProductList.remove(newProductList.size() - 1);
                itemCategoryAdapter.notifyItemRemoved(newProductList.size());
                page = page + 1;
                getData(page);
                itemCategoryAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
//        compositeDisposable.add(apiBanThuCung.getProducts(page, type)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        newProductModel -> {
//                            if(newProductModel.isSuccess()) {
//                                if(itemCategoryAdapter == null) {
//                                    newProductList = newProductModel.getResult();
//                                    itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), newProductList);
//                                    recyclerView.setAdapter(itemCategoryAdapter);
//                                } else {
//                                    int position = newProductList.size() - 1;
//                                    int quantityAdd = newProductModel.getResult().size();
//                                    for (int i = 0; i < quantityAdd; i++) {
//                                        newProductList.add(newProductModel.getResult().get(i));
//                                    }
//                                    itemCategoryAdapter.notifyItemRangeInserted(position, quantityAdd);
//                                }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Hết sản phẩm rồi bạn ơi!", Toast.LENGTH_SHORT).show();
//                            }
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), "Không kết nối server", Toast.LENGTH_LONG).show();
//                        }
//                ));

        apiBanThuCungSpringBoot.getProducts(page, type).enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if(response.body().isSuccess()) {
                    if(itemCategoryAdapter == null) {
                        newProductList = response.body().getResult();
                        itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), newProductList);
                        recyclerView.setAdapter(itemCategoryAdapter);
                    } else {
                        int position = newProductList.size() - 1;
                        int quantityAdd = response.body().getResult().size();
                        for (int i = 0; i < quantityAdd; i++) {
                            newProductList.add(response.body().getResult().get(i));
                        }
                        itemCategoryAdapter.notifyItemRangeInserted(position, quantityAdd);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Hết sản phẩm rồi bạn ơi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không kết nối server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(typeName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);

        toolbar = findViewById(R.id.toolbarItemCategory);
        recyclerView = findViewById(R.id.rc_itemCategory);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        newProductList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}