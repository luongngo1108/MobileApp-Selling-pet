package com.example.myapplication.retrofit;

import com.example.myapplication.models.CategoriesModel;
import com.example.myapplication.models.NewProductModel;
import com.example.myapplication.models.OrdersModel;
import com.example.myapplication.models.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIBanThuCung {
    @GET("getCategories.php")
    Observable<CategoriesModel> getCategories();

    @GET("getProducts.php")
    Observable<NewProductModel> getNewProduct();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<NewProductModel> getProducts(
            @Field("page") int page,
            @Field("type") int type
    );

    @POST("register.php")
    @FormUrlEncoded
    Observable<UserModel> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone
    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("forgotpassword.php")
    @FormUrlEncoded
    Observable<UserModel> forgotPassword(
            @Field("email") String email
    );

    @POST("orders.php")
    @FormUrlEncoded
    Observable<UserModel> createOrders(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("totalBill") String totalBill,
            @Field("iduser") int id,
            @Field("address") String address,
            @Field("quantity") int quantity,
            @Field("detail") String detail
    );

    @POST("order_history.php")
    @FormUrlEncoded
    Observable<OrdersModel> ordersHistory(
            @Field("iduser") int id
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<NewProductModel> search(
            @Field("search") String search
    );
}
