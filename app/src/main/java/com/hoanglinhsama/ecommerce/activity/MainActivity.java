package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.TypeProductAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityMainBinding;
import com.hoanglinhsama.ecommerce.model.TypeProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private TypeProductAdapter typeProductAdapter;
    private List<TypeProduct> typeProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setUpActionBar();
        setUpViewFlipper();
        initializationAdapter();
    }

    private void initializationAdapter() {
        this.typeProductList = new ArrayList<TypeProduct>();
        this.typeProductList.add(new TypeProduct("Trang Chủ", R.drawable.ic_home_page));
        this.typeProductList.add(new TypeProduct("Điện Thoại", R.drawable.ic_phone));
        this.typeProductList.add(new TypeProduct("LapTop", R.drawable.ic_laptop));
        this.typeProductList.add(new TypeProduct("Thông Tin", R.drawable.ic_information));
        this.typeProductList.add(new TypeProduct("Liên hệ", R.drawable.ic_contact));
        typeProductAdapter = new TypeProductAdapter(getApplicationContext(), R.layout.item_type_product, typeProductList);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // setDisplayHomeAsUpEnabled() de kich hoat se quay lai activity truoc khi chon Up
        activityMainBinding.toolBarMainScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityMainBinding.toolBarMainScreen.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.drawerLayoutMainScreen.openDrawer(GravityCompat.START);
            }
        });
    }
}