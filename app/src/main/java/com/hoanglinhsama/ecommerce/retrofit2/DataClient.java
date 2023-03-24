package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataClient {
    /**
     * Lay du lieu san pham moi tu server
     */
    @GET("getnewproduct.php")
    Call<List<Product>> getNewProduct();

    /**
     * Lay du lieu cua dien thoai (loai 1) hoac laptop (loai 2) tu server
     */
    @FormUrlEncoded
    @POST("getproductdetail.php")
    Call<List<Product>> getProductDetail(@Field("page") int page
            , @Field("type") int type);
}
