package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.model.Statistical;
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

/**
 * Gui request den Server PHPMyAdmin, va nhan lai response tra ve
 */
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
            , @Field("type") int type
            , @Field("firebaseUId") String firebaseUId);

    /**
     * Dang nhap
     */
    @FormUrlEncoded
    @POST("login.php")
    Call<List<User>> logIn(@Field("email") String email
            , @Field("password") String password);

    /**
     * Them du lieu vao bang don hang va don hang chi tiet tren server khi bam dat hang, truyen ca list cac san pham cho moi don hang
     */
    @FormUrlEncoded
    @POST("insertorderdetail.php")
    Call<String> insertOrderDetail(@Field("userId") int userId
            , @Field("address") String address
            , @Field("listCart") String listCart
            , @Field("totalMoney") long totalMoney
            , @Field("zaloPayTransactionId") String zaloPayTransactionId);

    /**
     * Cap nhat so luong san pham con lai khi dat hang, neu huy don hang thi hoan lai so luong da dat
     */
    @FormUrlEncoded
    @POST("updatequantityproduct.php")
    Call<String> updateQuantityProduct(@Field("productId") int productId
            , @Field("quantity") int quantity);

    /**
     * Lay lich su don hang
     */
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

    /**
     * Upload hinh anh san pham len server khi them moi san pham
     */
    @Multipart
    @POST("uploadpictureproduct.php")
    Call<String> upLoadPictureProduct(@Part MultipartBody.Part pictureProduct);

    /**
     * Cap nhat thong tin san pham khi tien hanh sua thong tin
     */
    @FormUrlEncoded
    @POST("updateproduct.php")
    Call<String> updateProduct(@Field("productId") int productId
            , @Field("nameProduct") String nameProduct
            , @Field("price") long price
            , @Field("quantity") int quantity
            , @Field("description") String description
            , @Field("type") int type
            , @Field("picture") String picture);

    /**
     * Cap nhat token cua user len database moi khi chay ung dung
     */
    @FormUrlEncoded // @FormUrlEncoded la Header cua request HTTP nay
    @POST("updatetoken.php")
    Call<String> updateToken(@Field("userId") int userId
            , @Field("token") String token);

    /**
     * Cap nhat lai trang thai cua don hang len database
     */
    @FormUrlEncoded
    @POST("updatestatusorder.php")
    Call<String> updateStatusOrder(@Field("orderId") int orderId
            , @Field("status") int status);

    /**
     * Lay ra token cua admin
     */
    @GET("gettokenadmin.php")
    Call<List<User>> getTokenAdmin();

    /**
     * Lay ra token cua user
     */
    @FormUrlEncoded
    @POST("gettokenuser.php")
    Call<List<User>> getTokenUser(@Field("orderId") int orderId);

    /**
     * Lay ra so luong da dat cua cac san pham de thong ke
     */
    @GET("incomestatistics.php")
    Call<List<Statistical>> statisticalIncome();

}
