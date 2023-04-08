package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hoanglinhsama.ecommerce.databinding.ActivityOrderBinding;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.text.DecimalFormat;

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
                            // Sau khi dat hang thanh cong thi dieu chinh lai so luong con lai cua san pham

                            // Xoa san pham khoi gio hang
                            startActivity(new Intent(OrderActivity.this, MainActivity.class));
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
}