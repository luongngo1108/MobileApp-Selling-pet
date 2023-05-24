package com.example.myapplication.retrofit;

import com.example.myapplication.models.Categories;
import com.example.myapplication.models.NewProduct;
import com.example.myapplication.models.NewProductModel;
import com.example.myapplication.models.OrdersModel;
import com.example.myapplication.models.ProductsModel;
import com.example.myapplication.models.UserModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIBanThuCungSpringBoot {
    @GET("getCategories")
    Call<List<Categories>> getCategories();

    @GET("getNewProducts")
    Call<List<NewProduct>> getNewProducts();

    @POST("getProducts")
    @FormUrlEncoded
    Call<ProductsModel> getProducts(
            @Field("page") int page,
            @Field("type") int type
    );

    @POST("register")
    @FormUrlEncoded
    Call<UserModel> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone
    );

    @POST("login")
    @FormUrlEncoded
    Call<UserModel> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("orders")
    @FormUrlEncoded
    Call<UserModel> createOrders(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("totalBill") String totalBill,
            @Field("iduser") int id,
            @Field("address") String address,
            @Field("quantity") int quantity,
            @Field("detail") String detail
    );

    @POST("order_history")
    @FormUrlEncoded
    Call<OrdersModel> ordersHistory(
            @Field("iduser") int id
    );

    @POST("search")
    @FormUrlEncoded
    Call<NewProductModel> search(
            @Field("search") String search
    );
}
