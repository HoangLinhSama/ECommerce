package com.hoanglinhsama.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.AdminFeatureAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityStatisticalBinding;
import com.hoanglinhsama.ecommerce.model.AdminFeature;
import com.hoanglinhsama.ecommerce.model.Statistical;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Thong ke nhung san pham duoc dat hang
 */
public class StatisticalActivity extends AppCompatActivity {
    private ActivityStatisticalBinding activityStatisticalBinding;
    private List<AdminFeature> listAdminFeature;
    private AdminFeatureAdapter adminFeatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStatisticalBinding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(activityStatisticalBinding.getRoot());

        setUpActionBar();
        getAdminFeature();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventClickNavigationDrawerMenu();
            getDataStatistical();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Thong ke doanh thu theo tung ngay trong thang
     */
    private void getDataStatistical() {
        List<BarEntry> listDataOrder = new ArrayList<>();
        DataClient dataClient = ApiUtils.getData();
        Call<List<Statistical>> call = dataClient.statisticalIncome();
        call.enqueue(new Callback<List<Statistical>>() {
            @Override
            public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                if (response.isSuccessful()) {
                    response.body().forEach(statistical -> listDataOrder.add(new BarEntry(statistical.getDayOrder(), statistical.getIncome())));
                    BarDataSet barDataSet = new BarDataSet(listDataOrder, "Doanh thu (₫)");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(17f);

                    BarData barData = new BarData(barDataSet);
                    activityStatisticalBinding.barChartStatisticalScreen.setFitBars(true);
                    activityStatisticalBinding.barChartStatisticalScreen.setData(barData);
                    activityStatisticalBinding.barChartStatisticalScreen.getDescription().setText("Biểu đồ thống kê doanh thu theo ngày trong một tháng");
                    activityStatisticalBinding.barChartStatisticalScreen.animateY(2000);
                }
            }

            @Override
            public void onFailure(Call<List<Statistical>> call, Throwable t) {
                Log.d("statisticalOrder", t.getMessage());
            }
        });
    }

    private void getAdminFeature() {
        listAdminFeature = new ArrayList<>();
        listAdminFeature.add(new AdminFeature("Trang Chủ", R.drawable.ic_home_page));
        listAdminFeature.add(new AdminFeature("Quản lý sản phẩm", R.drawable.ic_product_manage));
        listAdminFeature.add(new AdminFeature("Quản lý đơn hàng", R.drawable.ic_order_manage));
        listAdminFeature.add(new AdminFeature("Thống kê sản phẩm", R.drawable.ic_statistical));
        adminFeatureAdapter = new AdminFeatureAdapter(getApplicationContext(), R.layout.item_admin_feature, listAdminFeature);
        activityStatisticalBinding.listViewStatisticalScreen.setAdapter(adminFeatureAdapter);
    }

    private void getEventClickNavigationDrawerMenu() {
        activityStatisticalBinding.listViewStatisticalScreen.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(StatisticalActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    startActivity(new Intent(StatisticalActivity.this, ProductManageActivity.class));
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(StatisticalActivity.this, OrderManageActivity.class));
                    finish();
                    break;
                case 3:
                    startActivity(new Intent(StatisticalActivity.this, StatisticalActivity.class));
                    finish();
                    break;
            }
        });
    }

    private void setUpActionBar() {
        setSupportActionBar(activityStatisticalBinding.toolBarStatisticalScreen);
        activityStatisticalBinding.toolBarStatisticalScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityStatisticalBinding.toolBarStatisticalScreen.setNavigationOnClickListener(v -> activityStatisticalBinding.drawerLayoutStatisticalScreen.openDrawer(GravityCompat.START));
    }
}