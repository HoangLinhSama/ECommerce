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
import com.hoanglinhsama.ecommerce.eventbus.NotifyChangeOrder;
import com.hoanglinhsama.ecommerce.eventbus.TotalMoneyEvent;
import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

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

        this.setUpActionBar();
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
                Call<String> call = dataClient.insertOrderDetail(ApiUtils.currentUser.getId(), activityOrderBinding.editTextAddressOrderScreen.getText().toString().trim(), new Gson().toJson(ApiUtils.listCart)); // nho gson chuyen tu object java ve lai json de truyen qua php
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OrderActivity.this, "Đặt hàng thành công !", Toast.LENGTH_SHORT).show();
                            updateQuantityProduct(ApiUtils.listCart); // Cap nhat lai so luong con lai cua san pham
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

    private void deleteProductToCart(int userId, List<Cart> listCart) {
        DataClient dataClient = ApiUtils.getData();
        listCart.forEach(product -> {
            Call<String> call = dataClient.deleteCartDetail(userId, product.getIdProduct());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (product.getIdProduct() == listCart.get(listCart.size() - 1).getIdProduct()) { // phan tu cuoi cung cua list
                            getCartDetail();
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
                    EventBus.getDefault().post(new NotifyChangeOrder()); // Post event den eventbus de goi Adapter.notifydatasetchange()
                    EventBus.getDefault().post(new TotalMoneyEvent()); // Post event den eventbus de tinh toan lai tong tien,...
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.d("getCartDetail", t.getMessage());
                if (t.getMessage().equals("Expected BEGIN_ARRAY but was STRING at line 1 column 1 path $") || t.getMessage().equals("End of input at line 1 column 1 path $")) { // loi xay ra khi khong get duoc data tu table cart_detail (khi xoa tat ca san pham ra khoi gio hang), cach xu ly nay khong tot
                    ApiUtils.listCart.clear();
                    EventBus.getDefault().post(new NotifyChangeOrder());
                    EventBus.getDefault().post(new DisplayCartEvent()); // Post event den eventbus de hien thi recyclerview cart khi gio hnag trong
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
            Call<String> call = dataClient.updateProduct(product.getIdProduct(), product.getQuantityRemain() - product.getQuantity());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (product.getIdProduct() == listCart.get(listCart.size() - 1).getIdProduct()) { // phan tu cuoi cung cua list
                            deleteProductToCart(ApiUtils.currentUser.getId(), ApiUtils.listCart); // Xoa san pham da dat ra khoi gio hang
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