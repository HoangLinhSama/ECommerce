package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.ProductDetailAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityPhoneBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {
    private ActivityPhoneBinding activityPhoneBinding;
    private List<Product> listPhone;
    private ProductDetailAdapter phoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhoneBinding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(activityPhoneBinding.getRoot());

        setUpActionBar();
        this.getPhone();
    }

    private void setUpActionBar() {
        setSupportActionBar(activityPhoneBinding.toolBarPhoneScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // setDisplayHomeAsUpEnabled() de cho phpe kich hoat se quay lai activity truoc khi chon Up
        activityPhoneBinding.toolBarPhoneScreen.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Hien thi phone len recyclerview
     */
    private void getPhone() {
        int page = 1;
        int type = 1; // phone type 1, laptop type 2
        DataClient dataClientGetPhone = ApiUtils.getData();
        Call<List<Product>> callBackGetPhone = dataClientGetPhone.getProductDetail(page, type);
        callBackGetPhone.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body().equals("Fail !")) {
                    Toast.makeText(PhoneActivity.this, "Không thể hiển thị được điện thoại !", Toast.LENGTH_SHORT).show();
                } else {
                    listPhone = response.body();
                    phoneAdapter = new ProductDetailAdapter(getApplicationContext(), R.layout.item_product_detail, listPhone);
                    activityPhoneBinding.recyclerViewPhoneScreen.setAdapter(phoneAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    activityPhoneBinding.recyclerViewPhoneScreen.setLayoutManager(layoutManager);
                    activityPhoneBinding.recyclerViewPhoneScreen.setHasFixedSize(true);
                    activityPhoneBinding.recyclerViewPhoneScreen.addItemDecoration(new ItemDecoration(15));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PhoneActivity.this, "Không thể hiển thị được điện thoại !", Toast.LENGTH_SHORT).show();
                Log.d("getPhone", t.getMessage());
            }
        });
    }
}