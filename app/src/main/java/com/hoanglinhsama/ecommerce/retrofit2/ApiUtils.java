package com.hoanglinhsama.ecommerce.retrofit2;

import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.User;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {
    public static final String baseUrl = "http://192.168.1.73/ecommerce/"; // URL localhost PHP MyAdmin
    public static final String FCMUrl = "https://fcm.googleapis.com/"; // URL Firebase Cloud Message
    public static List<Cart> listCart; // list gio hang toan cuc chua thong tin cac san pham da them vao gio hang
    public static User currentUser = new User(); // tai khoan hien dang dang nhap ung dung
    public static List<Cart> listCartChecked = new ArrayList<>(); // list chua thong tin cac san pham ma checkbox cua no checked
    public static String receiveId; // Id cua nguoi nhan tin nhan

    /**
     * Key de khoi tao data trong document trong collection cua firebase cloud firestore
     */
    public static final String KEY_SEND = "sendId";
    public static final String KEY_RECEIVE = "receiveId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATE_TIME = "datetime";
    public static final String PATH_CHAT = "chat"; // duong dan cua collection chat (chua cac document ve cac tin nhan cua ca user va admin) trong firebase cloud firestore
    public static final String PATH_USER = "user"; // duong dan cua collection user (chua document ve danh sach cac id cua cac user duoc phep chat voi admin) trong firebase cloud firestore

    public static boolean isSignUp; // xac dinh truong hop se vao onResume() cua LogInActivity la do dang ky hay dang xuat

    public static DataClient getData() {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }

    public static DataPushNotification getDataNotification() {
        return RetrofitClient.getClient(FCMUrl).create(DataPushNotification.class);
    }
}
