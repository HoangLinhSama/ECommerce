package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.User;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {
    public static final String baseUrl = "http://192.168.1.237/ecommerce/";
    public static final String FCMUrl = "https://fcm.googleapis.com/";
    public static List<Cart> listCart; // list gio hang toan cuc chua thong tin cac san pham da them vao gio hang
    public static User currentUser = new User();
    public static List<Cart> listCartChecked = new ArrayList<>(); // list chua thong tin cac san pham ma checkbox cua no checked

    public static DataClient getData() {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }

    public static DataPushNotification getDataNotification() {
        return RetrofitClient.getClient(FCMUrl).create(DataPushNotification.class);
    }
}
