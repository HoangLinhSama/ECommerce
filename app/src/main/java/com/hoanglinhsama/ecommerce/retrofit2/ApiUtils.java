package com.hoanglinhsama.ecommerce.retrofit2;

public class ApiUtils {
    public static final String baseUrl = "http://192.168.1.237/ecommerce/";

    public static DataClient getData() {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}
