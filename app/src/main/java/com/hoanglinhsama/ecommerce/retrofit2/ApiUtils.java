package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;

import java.util.List;

public class ApiUtils {
    public static final String baseUrl = "http://192.168.1.237/ecommerce/";
    public static List<Cart> listCart; // list gio hang toan cuc chua thong tin cac san pham da them vao gio hang

    public static DataClient getData() {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}
