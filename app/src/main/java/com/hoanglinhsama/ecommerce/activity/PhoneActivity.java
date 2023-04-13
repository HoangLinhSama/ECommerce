package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private boolean isLoading = true; // isLoading la de kiem tra xem co dang load du lieu tu adapter len recyclerview khong, true la van con du lieu de load, false la da het du lieu va phai load more
    private Handler handler = new Handler();
    private LinearLayoutManager layoutManager;
    private int page = 1; // so trang bat dau tu trang so 1
    private int type = 1; // phone type 1, laptop type 2
    private int total = 3; // hien thi 3 phone tren mot trang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhoneBinding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(activityPhoneBinding.getRoot());

        if (MainActivity.isConnected(getApplicationContext())) {
            getPhone(page);
            addEventLoadMore();
            getEventClickBottomNavigationMenu();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClickBottomNavigationMenu() {
        activityPhoneBinding.bottomNavigationPhoneScreen.setSelectedItemId(R.id.menu_item_phone);
        activityPhoneBinding.bottomNavigationPhoneScreen.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_home_page:
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_phone:
                    startActivity(new Intent(PhoneActivity.this, PhoneActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_laptop:
                    startActivity(new Intent(PhoneActivity.this, LaptopActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_history_order:
                    startActivity(new Intent(PhoneActivity.this, OrderHistoryActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }

    private void addEventLoadMore() {
        activityPhoneBinding.recyclerViewPhoneScreen.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == listPhone.size() - 1) { // da scroll den phan tu cuoi cung hien co thi load them du lieu
                        isLoading = false; // can phai load them du lieu moi tu server ve
                        loadMore();
                    }
                }
            }
        });
    }

    /**
     * Load them phone khi da het phone cho recyclerview
     */
    private void loadMore() {
        handler.post(() -> {
            /* add null */
            listPhone.add(null);
            phoneAdapter.notifyItemInserted(listPhone.size() - 1);
        });
        handler.postDelayed(() -> {
            /* remove null */
            listPhone.remove(listPhone.size() - 1);
            phoneAdapter.notifyItemRemoved(listPhone.size());
            page = page + 1; // load du lieu cho trang tiep theo
            getPhone(page);
            isLoading = true; // da co them du lieu moi de load tu adapter len recyclerview
        }, 3000);
    }

    /**
     * Hien thi phone len recyclerview
     */
    private void getPhone(int page) {
        DataClient dataClientGetPhone = ApiUtils.getData();
        Call<List<Product>> callBackGetPhone = dataClientGetPhone.getProductDetail(page, type, total);
        callBackGetPhone.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    if (phoneAdapter == null) {
                        listPhone = response.body();
                        phoneAdapter = new ProductDetailAdapter(getApplicationContext(), listPhone);
                        activityPhoneBinding.recyclerViewPhoneScreen.setAdapter(phoneAdapter);
                        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        activityPhoneBinding.recyclerViewPhoneScreen.setLayoutManager(layoutManager);
                        activityPhoneBinding.recyclerViewPhoneScreen.addItemDecoration(new ItemDecoration(15));
                    } else {
                        listPhone.addAll(listPhone.size() - 1, response.body());
                        phoneAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PhoneActivity.this, "Không còn điện thoại để hiển thị !", Toast.LENGTH_SHORT).show();
                isLoading = false;
                Log.d("getPhone", t.getMessage());
            }
        });
    }
}