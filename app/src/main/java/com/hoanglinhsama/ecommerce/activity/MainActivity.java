package com.hoanglinhsama.ecommerce.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.NewProductAdapter;
import com.hoanglinhsama.ecommerce.adapter.TypeProductAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityMainBinding;
import com.hoanglinhsama.ecommerce.eventbus.LogOutEvent;
import com.hoanglinhsama.ecommerce.eventbus.NtfCountEvent;
import com.hoanglinhsama.ecommerce.eventbus.TotalMoneyEvent;
import com.hoanglinhsama.ecommerce.model.Cart;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.model.TypeProduct;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding activityMainBinding;
    private TypeProductAdapter typeProductAdapter;
    private List<TypeProduct> listTypeProduct;
    private List<Product> listNewProduct;
    private NewProductAdapter newProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        setUpActionBar();
        getTypeMenu();
        if (isConnected(this)) {
            setUpViewFlipper();
            getNewProduct();
            getEventClickNavigationDrawerMenu();
            getEventClickBottomNavigationMenu();
            getCartDetail();
            getEventClickImageViewCart();
            getEventLogout();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventLogout() {
        activityMainBinding.imageViewLogOut.setOnClickListener(v -> {
            EventBus.getDefault().post(new LogOutEvent());
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartDetail();
    }


    private void getEventClickImageViewCart() {
        activityMainBinding.imageViewCart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
    }

    /**
     * Bat su kien khi click vao menu cua bottom navigation bar
     */
    private void getEventClickBottomNavigationMenu() {
        activityMainBinding.bottomNavigationMainScreen.setSelectedItemId(R.id.menu_item_home_page);
        activityMainBinding.bottomNavigationMainScreen.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_home_page:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_item_phone:
                    startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                    return true;
                case R.id.menu_item_laptop:
                    startActivity(new Intent(MainActivity.this, LaptopActivity.class));
                    return true;
                case R.id.menu_item_history_order:
                    startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                    return true;
            }
            return false;
        });
    }

    /**
     * Lay du lieu gio hang cua nguoi dung tu server
     */
    public static void getCartDetail() {
        if (ApiUtils.listCart == null) {
            ApiUtils.listCart = new ArrayList<>();
        }
        DataClient dataClient = ApiUtils.getData();
        Call<List<Cart>> call = dataClient.getCartDetail(ApiUtils.currentUser.getId());
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    ApiUtils.listCart = response.body();
                    activityMainBinding.ntfCount.setText(String.valueOf(ApiUtils.listCart.size()));
                    EventBus.getDefault().post(new NtfCountEvent());
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.d("getCartDetail", t.getMessage());

                /* hien thi so san pham trong gio hang khi xoa het tat ca san pham khoi gio hang, sau do bam quay lai Main Activity, khi do onResume() cua MainActivity se chay */
                if (t.getMessage().equals("Expected BEGIN_ARRAY but was STRING at line 1 column 1 path $") || t.getMessage().equals("End of input at line 1 column 1 path $")) { // loi xay ra khi khong get duoc data tu table cart_detail (khi xoa tat ca san pham ra khoi gio hang)
                    activityMainBinding.ntfCount.clear();
                    EventBus.getDefault().post(new NtfCountEvent());
                }
            }
        });
    }

    /**
     * Bat su kien khi click vao menu cua navigation view
     */
    private void getEventClickNavigationDrawerMenu() {
        activityMainBinding.listViewMainScreen.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    break;
            }
        });
    }

    /**
     * Hien thi san pham moi len Recycler View
     */
    private void getNewProduct() {
        DataClient dataClientGetNewProduct = ApiUtils.getData();
        Call<List<Product>> callbackGetNewProduct = dataClientGetNewProduct.getNewProduct();
        callbackGetNewProduct.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    listNewProduct = response.body();
                    newProductAdapter = new NewProductAdapter(listNewProduct, R.layout.item_new_product, getApplicationContext());
                    activityMainBinding.recyclerViewMainScreen.setAdapter(newProductAdapter);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    activityMainBinding.recyclerViewMainScreen.setLayoutManager(layoutManager);
                    activityMainBinding.recyclerViewMainScreen.setHasFixedSize(true);
                    activityMainBinding.recyclerViewMainScreen.addItemDecoration(new ItemDecoration(10));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Không thể hiển thị được sản phẩm mới ! ", Toast.LENGTH_SHORT).show();
                Log.d("getNewProduct", t.getMessage());
            }
        });
    }

    /**
     * Tao menu cho Navigation Drawer
     */
    private void getTypeMenu() {
        this.listTypeProduct = new ArrayList<TypeProduct>();
        this.listTypeProduct.add(new TypeProduct("Trang Chủ", R.drawable.ic_home_page));
        this.listTypeProduct.add(new TypeProduct("Liên hệ", R.drawable.ic_contact));
        this.listTypeProduct.add(new TypeProduct("Thông Tin", R.drawable.ic_information));
        typeProductAdapter = new TypeProductAdapter(getApplicationContext(), R.layout.item_type_product, listTypeProduct);
        activityMainBinding.listViewMainScreen.setAdapter(typeProductAdapter);
    }

    /**
     * Tao View Flipper de quang cao san pham
     */
    private void setUpViewFlipper() {
        List<String> listAdvertisement = new ArrayList<>();
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/a54-720-220-720x220-9.png");
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/720-220-720x220-60.png");
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/reno8t-720-220-720x220-2.png");
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/02/banner/vivo-720-220-720x220.png");
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/Redmi-12c-720-220-720x220-6.png");
        listAdvertisement.add("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/chungr-MSI-720-220-720x220-1.png");
        listAdvertisement.forEach(element -> {
            ImageView imageViewAdvertisement = new ImageView(getApplicationContext());
            Picasso.get().load(element).into(imageViewAdvertisement);
            imageViewAdvertisement.setScaleType(ImageView.ScaleType.FIT_XY);
            activityMainBinding.viewFlipperMainScreen.addView(imageViewAdvertisement);
        });
        activityMainBinding.viewFlipperMainScreen.setFlipInterval(3000);
        activityMainBinding.viewFlipperMainScreen.setAutoStart(true);
        Animation animationSlideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.silde_in_right);
        Animation animationSlideOutRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        activityMainBinding.viewFlipperMainScreen.setInAnimation(animationSlideInRight);
        activityMainBinding.viewFlipperMainScreen.setOutAnimation(animationSlideOutRight);
    }

    /**
     * Khoi tao ActionBar (ToolBar)
     */
    private void setUpActionBar() {
        setSupportActionBar(activityMainBinding.toolBarMainScreen);
        // 2 dong duoi nay co the lam duoc viec nhu sau : neu tai khoan la loai cua nguoi ban thi chay 2 dong o duoi, con neu la tai khaon khach hang thi khong co 2 dong o duoi (vi du dinh dat chuc nang cua nguoi ban trong navigation drawer)
        activityMainBinding.toolBarMainScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityMainBinding.toolBarMainScreen.setNavigationOnClickListener(v -> activityMainBinding.drawerLayoutMainScreen.openDrawer(GravityCompat.START));
    }

    /**
     * Kiem tra xem co ket noi internet khong ?
     */
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (networkCapabilities != null) { // neu co ket noi (wifi hoac mobile data)
            return true;
        } else {
            return false;
        }
    }
}