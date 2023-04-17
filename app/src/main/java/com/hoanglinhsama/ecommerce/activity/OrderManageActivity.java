package com.hoanglinhsama.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.AdminFeatureAdapter;
import com.hoanglinhsama.ecommerce.adapter.OrderHistoryAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityOrderManageBinding;
import com.hoanglinhsama.ecommerce.model.AdminFeature;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Quan ly cac don hang da dat cua user
 */
public class OrderManageActivity extends AppCompatActivity {
    private ActivityOrderManageBinding activityOrderManageBinding;
    private List<AdminFeature> listAdminFeature;
    private AdminFeatureAdapter adminFeatureAdapter;
    private List<Order> listOrder;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderManageBinding = ActivityOrderManageBinding.inflate(getLayoutInflater());
        setContentView(activityOrderManageBinding.getRoot());

        setUpActionBar();
        getAdminFeature();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventClickNavigationDrawerMenu();
            getOrderHistory();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Lay ra lich su tat ca don hang cua khach hang da dat
     */
    private void getOrderHistory() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Order>> call = dataClient.getOrderHistory(0); // admin muon lay toan bo don hang cua cac user da dat
        {
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful()) {
                        listOrder = response.body();
                        orderHistoryAdapter = new OrderHistoryAdapter(listOrder, R.layout.item_order_history, getApplicationContext());
                        activityOrderManageBinding.recyclerViewOrderManageScreen.setAdapter(orderHistoryAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        activityOrderManageBinding.recyclerViewOrderManageScreen.setLayoutManager(linearLayoutManager);
                        activityOrderManageBinding.recyclerViewOrderManageScreen.addItemDecoration(new ItemDecoration(15));
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Log.d("getOrderHistory", t.getMessage());
                }
            });
        }
    }

    private void getEventClickNavigationDrawerMenu() {
        activityOrderManageBinding.listViewOrderManageScreen.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(OrderManageActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    startActivity(new Intent(OrderManageActivity.this, ProductManageActivity.class));
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(OrderManageActivity.this, OrderManageActivity.class));
                    finish();
                    break;
            }
        });
    }

    private void getAdminFeature() {
        listAdminFeature = new ArrayList<>();
        listAdminFeature.add(new AdminFeature("Trang Chủ", R.drawable.ic_home_page));
        listAdminFeature.add(new AdminFeature("Quản lý sản phẩm", R.drawable.ic_product_manage));
        listAdminFeature.add(new AdminFeature("Quản lý đơn hàng", R.drawable.ic_order_manage));
        adminFeatureAdapter = new AdminFeatureAdapter(getApplicationContext(), R.layout.item_admin_feature, listAdminFeature);
        activityOrderManageBinding.listViewOrderManageScreen.setAdapter(adminFeatureAdapter);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityOrderManageBinding.toolBarOrderManageScreen);
        activityOrderManageBinding.toolBarOrderManageScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityOrderManageBinding.toolBarOrderManageScreen.setNavigationOnClickListener(v -> activityOrderManageBinding.drawerLayoutOrderManageScreen.openDrawer(GravityCompat.START));
    }
}