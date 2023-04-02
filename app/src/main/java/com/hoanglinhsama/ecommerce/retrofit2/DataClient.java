package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;
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
            , @Field("type") int type
            , @Field("total") int total);

    /**
     * Lay du lieu ve gio hang cua nguoi dung dua vao userId di kem theo bo username password khi dang nhap thanh cong
     */
    @FormUrlEncoded
    @POST("getcartdetail.php")
    Call<List<Cart>> getCartDetail(@Field("userId") int userId);

    /**
     * Them du lieu vao bang cart_detail tren server
     */
    @FormUrlEncoded
    @POST("insertcartdetail.php")
    Call<String> insertCartDetail(@Field("userId") int userId
            , @Field("productId") int productId
            , @Field("quantity") int quantity);

    /**
     * Update du lieu vao bang cart_detail tren server
     */
    @FormUrlEncoded
    @POST("updatecartdetail.php")
    Call<String> updateCartDetail(@Field("userId") int userId
            , @Field("productId") int productId
            , @Field("quantity") int quantity);

    /**
     * Delete du lieu trong bang cart_detail tren server
     */
    @FormUrlEncoded
    @POST("deletecartdetail.php")
    Call<String> deleteCartDetail(@Field("userId") int userId
            , @Field("productId") int productId);

    /**
     * Dang ky tai khoan
     */
    @FormUrlEncoded
    @POST("signup.php")
    Call<String> signUp(@Field("email") String email
            , @Field("password") String password
            , @Field("name") String name
            , @Field("phoneNumber") String phoneNumber
            , @Field("type") int type);
}
