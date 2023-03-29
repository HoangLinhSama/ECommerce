package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.adapter.ProductDetailAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityLaptopBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaptopActivity extends AppCompatActivity {
    private ActivityLaptopBinding activityLaptopBinding;
    private List<Product> listLaptop;
    private ProductDetailAdapter laptopAdapter;
    private boolean isLoading = true;
    private Handler handler = new Handler();
    private LinearLayoutManager layoutManager;
    int page = 1; // so trang bat dau tu trang so 1
    int type = 2; // phone type 1, laptop type 2
    int total = 5; // hien thi 5 laptop tren mot trang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLaptopBinding = ActivityLaptopBinding.inflate(getLayoutInflater());
        setContentView(activityLaptopBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {

            this.getLaptop(page);
            this.addEventLoadMore();
        } else {
            Toast.makeText(this, "No Internet ! Please connect !", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEventLoadMore() {
        activityLaptopBinding.recyclerViewLaptopScreen.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == listLaptop.size() - 1) {
                        isLoading = false;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(() -> {
            listLaptop.add(null);
            laptopAdapter.notifyItemInserted(listLaptop.size() - 1);
        });
        handler.postDelayed(() -> {
            listLaptop.remove(listLaptop.size() - 1);
            laptopAdapter.notifyItemRemoved(listLaptop.size());
            page = page + 1;
            getLaptop(page);
            isLoading = true;
        }, 3000);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityLaptopBinding.toolBarLaptopScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityLaptopBinding.toolBarLaptopScreen.setNavigationOnClickListener(v -> finish());
    }

    private void getLaptop(int page) {
        DataClient dataClientGetPhone = ApiUtils.getData();
        Call<List<Product>> callBackGetPhone = dataClientGetPhone.getProductDetail(page, type, total);
        callBackGetPhone.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    if (laptopAdapter == null) {
                        listLaptop = response.body();
                        laptopAdapter = new ProductDetailAdapter(getApplicationContext(), listLaptop);
                        activityLaptopBinding.recyclerViewLaptopScreen.setAdapter(laptopAdapter);
                        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        activityLaptopBinding.recyclerViewLaptopScreen.setLayoutManager(layoutManager);
                        activityLaptopBinding.recyclerViewLaptopScreen.addItemDecoration(new ItemDecoration(15));
                    } else {
                        listLaptop.addAll(listLaptop.size() - 1, response.body());
                        laptopAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(LaptopActivity.this, "Không còn Laptop để hiển thị !", Toast.LENGTH_SHORT).show();
                isLoading = false;
                Log.d("getLaptop", t.getMessage());
            }
        });
    }
}