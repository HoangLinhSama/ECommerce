package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    /**
     * Dang nhap
     */
    @FormUrlEncoded
    @POST("login.php")
    Call<List<User>> logIn(@Field("email") String email
            , @Field("password") String password);

    /**
     * Reset lai password khi quen
     */
    @FormUrlEncoded
    @POST("sendlinkresetpassword.php")
    Call<String> resetPassword(@Field("email") String email);

    /**
     * Them du lieu vao bang don hang va don hang chi tiet tren server khi bam dat hang, truyen ca list cac san pham cho moi don hang
     */
    @FormUrlEncoded
    @POST("insertorderdetail.php")
    Call<String> insertOrderDetail(@Field("userId") int userId
            , @Field("address") String address
            , @Field("listCart") String listCart);

    /**
     * Cap nhat so luong san pham con lai khi dat hang, neu huy don hang thi hoan lai so luong da dat
     */
    @FormUrlEncoded
    @POST("updateproduct.php")
    Call<String> updateProduct(@Field("productId") int productId
            , @Field("quantity") int quantity);

    @FormUrlEncoded
    @POST("getorderhistory.php")
    Call<List<Order>> getOrderHistory(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("searchproduct.php")
    Call<List<Product>> searchProduct(@Field("nameProduct") String nameProduct);

    /**
     * Them san pham moi vao table product
     */
    @FormUrlEncoded
    @POST("addproduct.php")
    Call<String> addProduct(@Field("name") String name
            , @Field("price") long price
            , @Field("picture") String picture
            , @Field("description") String description
            , @Field("type") int type
            , @Field("quantity") int quantity);

    @Multipart
    @POST("uploadpictureproduct.php")
    Call<String> upLoadPictureProduct(@Part MultipartBody.Part pictureProduct);
}
