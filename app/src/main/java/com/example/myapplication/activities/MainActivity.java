package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CategoriesAdapter;
import com.example.myapplication.adapter.NewProductAdapter;
import com.example.myapplication.models.Categories;
import com.example.myapplication.models.NewProduct;
import com.example.myapplication.models.User;
import com.example.myapplication.retrofit.APIBanThuCung;
import com.example.myapplication.retrofit.APIBanThuCungSpringBoot;
import com.example.myapplication.retrofit.RetrofitClient;
import com.example.myapplication.retrofit.RetrofitSpringBoot;
import com.example.myapplication.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewCategories;
    DrawerLayout drawerLayout;
    CategoriesAdapter categoriesAdapter;
    List<Categories> listCategories;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanThuCung apiBanThuCung;
    List<NewProduct> listNewProduct;
    NewProductAdapter newProductAdapter;
    NotificationBadge notificationBadge;
    FrameLayout frameCart;
    ImageView img_search;

    RetrofitSpringBoot retrofitSpringBoot;
    APIBanThuCungSpringBoot apiBanThuCungSpringBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }

        Anhxa();
        ActionBar();

        if(isConnected(this)) {
            ActionViewFlipper();
            getCategories();
            getNewProduct();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Wifi off, vui lòng bật internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homepage);
                        break;
                    case 1:
                        Intent dogActivity = new Intent(getApplicationContext(), ItemCategoryActivity.class);
                        dogActivity.putExtra("type", 1);
                        dogActivity.putExtra("typeName", listCategories.get(1).getName());
                        startActivity(dogActivity);
                        break;
                    case 2:
                        Intent catActivity = new Intent(getApplicationContext(), ItemCategoryActivity.class);
                        catActivity.putExtra("type", 2);
                        catActivity.putExtra("typeName", listCategories.get(2).getName());
                        startActivity(catActivity);
                        break;
                    case 3:
                        Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
                        profileActivity.putExtra("user_id", Utils.user_current.getId());
                        profileActivity.putExtra("user_name", Utils.user_current.getName());
                        profileActivity.putExtra("user_email", Utils.user_current.getEmail());
                        profileActivity.putExtra("user_image", Utils.user_current.getImage());
                        startActivity(profileActivity);
                        break;
                    case 5:
                        Intent orderHistoryActivity = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        startActivity(orderHistoryActivity);
                        break;
                    case 6:
                        // Xóa key user
                        Paper.book().delete("user");
                        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginActivity);
                        break;
                }
            }
        });
    }

    private void getNewProduct() {
//        compositeDisposable.add(apiBanThuCung.getNewProduct()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        newProductModel -> {
//                            listNewProduct = newProductModel.getResult();
//                            newProductAdapter = new NewProductAdapter(getApplicationContext(), listNewProduct);
//                            recyclerView.setAdapter(newProductAdapter);
//                        },
//                        throwable -> {
//                            Toast.makeText(getApplicationContext(), "Không kết nối được với server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                ));

        apiBanThuCungSpringBoot.getNewProducts().enqueue(new Callback<List<NewProduct>>() {
            @Override
            public void onResponse(Call<List<NewProduct>> call, Response<List<NewProduct>> response) {
                listNewProduct = response.body();
                newProductAdapter = new NewProductAdapter(getApplicationContext(), listNewProduct);
                recyclerView.setAdapter(newProductAdapter);
            }

            @Override
            public void onFailure(Call<List<NewProduct>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không kết nối được với server" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCategories() {

//        compositeDisposable.add(apiBanThuCung.getCategories()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    categoriesModel -> {
//                        if (categoriesModel.isSuccess()) {
//                            listCategories = categoriesModel.getResult();
////                            Log.d("logg", String.valueOf(listCategories.size()));
//                            // Khởi tạo adapter
//                            categoriesAdapter = new CategoriesAdapter(getApplicationContext(), listCategories);
//                            listViewCategories.setAdapter(categoriesAdapter);
//                        }
//                    }
//                ));

        apiBanThuCungSpringBoot.getCategories().enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                listCategories = response.body();
                listCategories.add(new Categories("Đăng xuất", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6M_fC7k3QEi2mij11PVHjxXFpv06b0OpbHw&usqp=CAU"));
                categoriesAdapter = new CategoriesAdapter(getApplicationContext(), listCategories);
                listViewCategories.setAdapter(categoriesAdapter);
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Log.d("Loggg", t.getMessage());
            }
        });
    }

    private void Anhxa() {
        apiBanThuCung = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanThuCung.class);

        retrofitSpringBoot = new RetrofitSpringBoot();
        apiBanThuCungSpringBoot = retrofitSpringBoot.getRetrofit().create(APIBanThuCungSpringBoot.class);

        toolbar = findViewById(R.id.toolbar_main);
        viewFlipper = findViewById(R.id.viewFlipper_main);
        recyclerView = findViewById(R.id.rc_newproduct);
        navigationView = findViewById(R.id.navigationView_main);
        listViewCategories = findViewById(R.id.listView_main);
        drawerLayout = findViewById(R.id.drawer_main);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        frameCart = findViewById(R.id.frame_cart);
        notificationBadge = findViewById(R.id.menuNotify);
        img_search = findViewById(R.id.img_search);
        // Khởi tạo list
        listCategories = new ArrayList<>();
        listNewProduct = new ArrayList<>();
        if (Utils.listCart == null) {
            Utils.listCart = new ArrayList<>();
        } else {
            int totalItem = 0;
            for(int i = 0; i < Utils.listCart.size(); i++) {
                totalItem = totalItem + Utils.listCart.get(i).getQuantity();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }

        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i = 0; i < Utils.listCart.size(); i++) {
            totalItem = totalItem + Utils.listCart.get(i).getQuantity();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void ActionViewFlipper() {
        List<String> flipperQuangCao = new ArrayList<>();
        flipperQuangCao.add("https://azpet.com.vn/wp-content/uploads/2022/03/azpet-banner-desktop-1.jpg");
        flipperQuangCao.add("https://azpet.com.vn/wp-content/uploads/2022/03/banner-desktop-1.jpg");
        flipperQuangCao.add("https://azpet.com.vn/wp-content/uploads/2022/03/banner-desktop-2.jpg");
        for (int i = 0; i < flipperQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(flipperQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}