package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hoanglinhsama.ecommerce.databinding.ActivityOrderBinding;
import com.hoanglinhsama.ecommerce.eventbus.DisplayCartEvent;
import com.hoanglinhsama.ecommerce.eventbus.NotifyChangeOrderEvent;
import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.NotificationReceiveData;
import com.hoanglinhsama.ecommerce.model.NotificationSendData;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.hoanglinhsama.ecommerce.retrofit2.DataPushNotification;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding activityOrderBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderBinding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(activityOrderBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            getEventOrder();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long totalMoney = intent.getLongExtra("totalMoney", 0);
        activityOrderBinding.textViewTotalPriceOrderScreen.setText(decimalFormat.format(Double.parseDouble(String.valueOf(totalMoney))) + "₫");
        activityOrderBinding.textViewPhoneOrderScreen.setText(ApiUtils.currentUser.getPhoneNumber());
        activityOrderBinding.textViewEmailOrderScreen.setText(ApiUtils.currentUser.getEmail());
    }

    private void setUpActionBar() {
        setSupportActionBar(activityOrderBinding.toolBarOrderScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityOrderBinding.toolBarOrderScreen.setNavigationOnClickListener(v -> finish());
    }

    private void getEventOrder() {
        activityOrderBinding.buttonOrderScreen.setOnClickListener(v -> {
            if (TextUtils.isEmpty(activityOrderBinding.editTextAddressOrderScreen.getText().toString().trim())) {
                Toast.makeText(this, "Chưa nhập địa chỉ !", Toast.LENGTH_SHORT).show();
            } else {
                DataClient dataClient = ApiUtils.getData();
                Call<String> call = dataClient.insertOrderDetail(ApiUtils.currentUser.getId(), activityOrderBinding.editTextAddressOrderScreen.getText().toString().trim(), new Gson().toJson(ApiUtils.listCartChecked)); // nho gson chuyen tu object java ve lai json de truyen qua php, ( truyen vao list cac san pham duoc checked )
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            pushNotificationToAdmin(); // gui thong bao don hang den admin
                            Toast.makeText(OrderActivity.this, "Đặt hàng thành công !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("getEventOrder", t.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Gui mot thong bao den nguoi ban (admin)
     */
    private void pushNotificationToAdmin() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<User>> call = dataClient.getTokenAdmin();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    String tokenAdmin = response.body().get(0).getToken(); // logic la : 1 admin, nhieu user, nen chi gui thong bao den 1 admin
                    Map<String, String> notification = new HashMap<>();
                    notification.put("tile", "Đơn hàng");
                    notification.put("body", "Có đơn hàng mới !");
                    NotificationSendData notificationSendData = new NotificationSendData(tokenAdmin, notification);
                    DataPushNotification dataPushNotification = ApiUtils.getDataNotification();
                    Call<NotificationReceiveData> call1 = dataPushNotification.sendNotification(notificationSendData);
                    call1.enqueue(new Callback<NotificationReceiveData>() {
                        @Override
                        public void onResponse(Call<NotificationReceiveData> call, Response<NotificationReceiveData> response) {
                            if (response.isSuccessful()) {
                                updateQuantityProduct(ApiUtils.listCartChecked);
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationReceiveData> call, Throwable t) {
                            Log.d("sendNotification", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("getTokenAdmin", t.getMessage());
            }
        });
    }

    private void deleteProductToCart(int userId, List<Cart> listCart) {
        DataClient dataClient = ApiUtils.getData();
        listCart.forEach(product -> {
            Call<String> call = dataClient.deleteCartDetail(userId, product.getIdProduct());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (product.getIdProduct() == listCart.get(listCart.size() - 1).getIdProduct()) { // phan tu cuoi cung cua list
                            getCartDetail(); // cap nhat lai cac san pham trong gio hang
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("deleteProductToCart", t.getMessage());
                }
            });
        });
    }

    private void getCartDetail() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Cart>> call = dataClient.getCartDetail(ApiUtils.currentUser.getId());
        startActivity(new Intent(OrderActivity.this, MainActivity.class)); // Xong het thi quay lai man hinh chinh
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    ApiUtils.listCart = response.body();
                    EventBus.getDefault().post(new NotifyChangeOrderEvent()); // Post event den eventbus de goi Adapter.notifydatasetchange()
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.d("getCartDetail", t.getMessage());
                if (t.getMessage().equals("Expected BEGIN_ARRAY but was STRING at line 1 column 1 path $") || t.getMessage().equals("End of input at line 1 column 1 path $")) { // loi xay ra khi khong get duoc data tu table cart_detail (khi xoa tat ca san pham ra khoi gio hang), cach xu ly nay khong tot
                    ApiUtils.listCart.clear();
                    EventBus.getDefault().post(new NotifyChangeOrderEvent());
                    EventBus.getDefault().post(new DisplayCartEvent()); // Post event den eventbus de hien thi recyclerview cart khi gio hang trong
                }
            }
        });

    }

    /**
     * Cap nhat lai so luong con lai cua san pham
     */
    private void updateQuantityProduct(List<Cart> listCart) {
        DataClient dataClient = ApiUtils.getData();
        listCart.forEach(product -> {
            Call<String> call = dataClient.updateQuantityProduct(product.getIdProduct(), product.getQuantityRemain() - product.getQuantity());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (product.getIdProduct() == listCart.get(listCart.size() - 1).getIdProduct()) { // phan tu cuoi cung cua list
                            deleteProductToCart(ApiUtils.currentUser.getId(), ApiUtils.listCartChecked); // Xoa san pham da dat ra khoi gio hang
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("updateQuantityProduct", t.getMessage());
                }
            });
        });
    }
}