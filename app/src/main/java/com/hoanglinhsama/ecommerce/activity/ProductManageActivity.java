package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.AdminFeatureAdapter;
import com.hoanglinhsama.ecommerce.adapter.ManageProductAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityProductManageBinding;
import com.hoanglinhsama.ecommerce.eventbus.DeleteModifyProductEvent;
import com.hoanglinhsama.ecommerce.model.AdminFeature;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManageActivity extends AppCompatActivity {
    private ActivityProductManageBinding activityProductManageBinding;
    private List<AdminFeature> listAdminFeature;
    private AdminFeatureAdapter adminFeatureAdapter;
    private List<Product> listProduct;
    private ManageProductAdapter manageProductAdapter;
    private Product productDeleteModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductManageBinding = ActivityProductManageBinding.inflate(getLayoutInflater());
        setContentView(activityProductManageBinding.getRoot());

        setUpActionBar();
        getAdminFeature();
        if (MainActivity.isConnected(getApplicationContext())) {
            getProduct();
            getEventClickNavigationDrawerMenu();
            getEventAddProduct();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void getProduct() {
        DataClient dataClientGetNewProduct = ApiUtils.getData();
        Call<List<Product>> callbackGetNewProduct = dataClientGetNewProduct.getNewProduct();
        callbackGetNewProduct.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    listProduct = response.body();
                    manageProductAdapter = new ManageProductAdapter(listProduct, R.layout.item_new_product, getApplicationContext());
                    activityProductManageBinding.recyclerViewProductManageScreen.setAdapter(manageProductAdapter);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    activityProductManageBinding.recyclerViewProductManageScreen.setLayoutManager(layoutManager);
                    activityProductManageBinding.recyclerViewProductManageScreen.addItemDecoration(new ItemDecoration(13));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductManageActivity.this, "Không thể hiển thị được sản phẩm ! ", Toast.LENGTH_SHORT).show();
                Log.d("getProduct", t.getMessage());
            }
        });
    }

    /**
     * Bat su kien them san pham moi
     */
    private void getEventAddProduct() {
        activityProductManageBinding.imageViewAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(ProductManageActivity.this, AddProductActivity.class));
        });
    }

    private void getAdminFeature() {
        listAdminFeature = new ArrayList<>();
        listAdminFeature.add(new AdminFeature("Trang Chủ", R.drawable.ic_home_page));
        listAdminFeature.add(new AdminFeature("Quản lý sản phẩm", R.drawable.ic_product_manage));
        listAdminFeature.add(new AdminFeature("Quản lý đơn hàng", R.drawable.ic_order_manage));
        listAdminFeature.add(new AdminFeature("Thống kê sản phẩm", R.drawable.ic_statistical));
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
                case 2:
                    startActivity(new Intent(ProductManageActivity.this, OrderManageActivity.class));
                    finish();
                    break;
                case 3:
                    startActivity(new Intent(ProductManageActivity.this, StatisticalActivity.class));
                    finish();
                    break;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 0) {
            modifyProduct();
        } else {
            deleteProduct();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteProduct() { // Khong nen xoa san pham vi se bi loi foreign key, set quantity =0 de khong hien thi len client la duoc
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.updateQuantityProduct(productDeleteModify.getId(), 0);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductManageActivity.this, "Xóa sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                    getProduct(); // cap nhat lai recyclerViewProductManage
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("deleteProduct", t.getMessage());
            }
        });
    }

    private void modifyProduct() {
        Intent intent = new Intent(ProductManageActivity.this, AddProductActivity.class);
        intent.putExtra("Product Modify", productDeleteModify);
        startActivity(intent);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityProductManageBinding.toolBarProductManageScreen);
        activityProductManageBinding.toolBarProductManageScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityProductManageBinding.toolBarProductManageScreen.setNavigationOnClickListener(v -> activityProductManageBinding.drawerLayoutProductManageScreen.openDrawer(GravityCompat.START));
    }

    /**
     * Xu ly su kien xoa sua do ManageProductAdapter phat ra
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteModifyProduct(DeleteModifyProductEvent event) {
        if (event != null) {
            productDeleteModify = event.getProduct();
        }
    }

}