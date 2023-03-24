package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataClient {
    /**
     * Lay du lieu san pham tu server ve
     */
    @GET("getproduct.php")
    Call<List<Product>> getProduct();
}
