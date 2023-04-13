package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.OrderHistoryAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityOrderHistoryBinding;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private ActivityOrderHistoryBinding activityOrderHistoryBinding;
    private List<Order> listOrder;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderHistoryBinding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityOrderHistoryBinding.getRoot());

        if (MainActivity.isConnected(getApplicationContext())) {
            getOrderHistory();
            getEventClickBottomNavigationMenu();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClickBottomNavigationMenu() {
        activityOrderHistoryBinding.bottomNavigationOrderHistoryScreen.setSelectedItemId(R.id.menu_item_history_order);
        activityOrderHistoryBinding.bottomNavigationOrderHistoryScreen.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_home_page:
                    startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_phone:
                    startActivity(new Intent(OrderHistoryActivity.this, PhoneActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_laptop:
                    startActivity(new Intent(OrderHistoryActivity.this, LaptopActivity.class));
                    finish();
                    return true;
                case R.id.menu_item_history_order:
                    startActivity(new Intent(OrderHistoryActivity.this, OrderHistoryActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }

    /**
     * Lay thong tin ve cac don hang ma khach hang da dat
     */
    private void getOrderHistory() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Order>> call = dataClient.getOrderHistory(ApiUtils.currentUser.getId());
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    listOrder = response.body();
                    orderHistoryAdapter = new OrderHistoryAdapter(listOrder, R.layout.item_order_history, getApplicationContext());
                    activityOrderHistoryBinding.recyclerViewOrderHistoryScreen.setAdapter(orderHistoryAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    activityOrderHistoryBinding.recyclerViewOrderHistoryScreen.setLayoutManager(linearLayoutManager);
                    activityOrderHistoryBinding.recyclerViewOrderHistoryScreen.addItemDecoration(new ItemDecoration(15));
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.d("getOrderHistory", t.getMessage());
            }
        });
    }
}