package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.AdminFeatureAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityProductManageBinding;
import com.hoanglinhsama.ecommerce.model.AdminFeature;

import java.util.ArrayList;
import java.util.List;

public class ProductManageActivity extends AppCompatActivity {
    private ActivityProductManageBinding activityProductManageBinding;
    private List<AdminFeature> listAdminFeature;
    private AdminFeatureAdapter adminFeatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductManageBinding = ActivityProductManageBinding.inflate(getLayoutInflater());
        setContentView(activityProductManageBinding.getRoot());

        setUpActionBar();
        getAdminFeature();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventClickNavigationDrawerMenu();
            getEventAddProduct();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Bat su kien them san pham moi
     */
    private void getEventAddProduct() {
        activityProductManageBinding.neumorphCardViewAdd.setOnClickListener(v -> {
            startActivity(new Intent(ProductManageActivity.this, AddProductActivity.class));
        });
    }

    private void getAdminFeature() {
        listAdminFeature = new ArrayList<>();
        listAdminFeature.add(new AdminFeature("Trang Chủ", R.drawable.ic_home_page));
        listAdminFeature.add(new AdminFeature("Quản lý sản phẩm", R.drawable.ic_product_manage));
        adminFeatureAdapter = new AdminFeatureAdapter(getApplicationContext(), R.layout.item_admin_feature, listAdminFeature);
        activityProductManageBinding.listViewProductManageScreen.setAdapter(adminFeatureAdapter);
    }

    private void getEventClickNavigationDrawerMenu() {
        activityProductManageBinding.listViewProductManageScreen.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(ProductManageActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    startActivity(new Intent(ProductManageActivity.this, ProductManageActivity.class));
                    finish();
                    break;
            }
        });
    }

    private void setUpActionBar() {
        setSupportActionBar(activityProductManageBinding.toolBarProductManageScreen);
        activityProductManageBinding.toolBarProductManageScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityProductManageBinding.toolBarProductManageScreen.setNavigationOnClickListener(v -> activityProductManageBinding.drawerLayoutProductManageScreen.openDrawer(GravityCompat.START));
    }

}