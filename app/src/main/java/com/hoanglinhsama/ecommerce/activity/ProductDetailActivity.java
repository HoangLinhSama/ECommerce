package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivityProductDetailBinding;
import com.hoanglinhsama.ecommerce.eventbus.NtfCountEvent;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    public static ActivityProductDetailBinding activityProductDetailBinding;
    public static Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(activityProductDetailBinding.getRoot());

        featureUserAdmin();
        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            addToCart();
            getEventClickImageViewCart();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void featureUserAdmin() {
        if (ApiUtils.currentUser.getType() == 1) { // admin
            activityProductDetailBinding.buttonAddToCart.setVisibility(View.INVISIBLE);
            activityProductDetailBinding.frameLayoutProductDetailScreen.setVisibility(View.INVISIBLE);
            activityProductDetailBinding.spinnerProductDetailScreen.setVisibility(View.INVISIBLE);
        } else // user
        {
            activityProductDetailBinding.linearLayoutQuantityProductDetail.setVisibility(View.INVISIBLE); // khong cho nguoi dung thay so luong san pham con lai
        }
    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.getCartDetail();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Bat su kien them san pham vao gio hang
     */
    private void addToCart() {
        activityProductDetailBinding.buttonAddToCart.setOnClickListener(v -> {
            AtomicInteger quantityChoose = new AtomicInteger(Integer.parseInt(activityProductDetailBinding.spinnerProductDetailScreen.getSelectedItem().toString())); // so luong san pham chon
            AtomicInteger quantity = new AtomicInteger(); // so luong thuc te cua san pham trong gio hang
            /* So luong toi da cua san pham co the them vao gio hang */
            if (quantityChoose.get() > product.getQuantity()) {
                Toast.makeText(this, "Số lượng muốn thêm vào giỏ hàng vượt quá số lượng còn lại !", Toast.LENGTH_SHORT).show();
                quantityChoose.set(product.getQuantity());
            }
            if (ApiUtils.listCart.size() > 0) { // da co it nhat 1 san pham trong gio hang
                AtomicBoolean exist = new AtomicBoolean(false);
                ApiUtils.listCart.forEach(cart -> {
                    if (product.getId() == cart.getIdProduct()) {
                        quantity.set(cart.getQuantity() + quantityChoose.get());
                        if (quantity.get() > product.getQuantity()) {
                            Toast.makeText(this, "Sản phẩm đã đạt số lượng tối đa !", Toast.LENGTH_SHORT).show();
                            quantity.set(product.getQuantity());
                        }
                        updateProductToCart(quantity.get());
                        exist.set(true);
                    }
                });
                if (!exist.get()) { // loai san pham moi them vao chua ton tai trong gio hang
                    addNewProductToCart(quantityChoose.get());
                }
            } else { // gio hang hien dang trong
                addNewProductToCart(quantityChoose.get());
            }
        });
    }

    /**
     * Cap nhat lai thong tin loai san pham da ton tai trong gio hang
     */
    private void updateProductToCart(int quantity) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.updateCartDetail(ApiUtils.currentUser.getId(), product.getId(), quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MainActivity.getCartDetail();
                    Toast.makeText(ProductDetailActivity.this, "Thêm sản phẩm vào giỏ hàng thành công !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("updateProductToCart", t.getMessage());
            }
        });
    }

    /**
     * Them loai san pham chua ton tai trong gio hang vao gio hang
     */
    private void addNewProductToCart(int quantity) {
        /* Them du lieu vao bang cart_detail */
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.insertCartDetail(ApiUtils.currentUser.getId(), product.getId(), quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    /* Load lai du lieu cac loai san pham hien co trong gio hang tu server ve */
                    MainActivity.getCartDetail();
                    Toast.makeText(ProductDetailActivity.this, "Thêm sản phẩm vào giỏ hàng thành công !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("addNewProductToCart", t.getMessage());
            }
        });
    }

    private void getEventClickImageViewCart() {
        activityProductDetailBinding.imageViewCart.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
        });
    }

    /**
     * Nhan du lieu tu intent de hien thi len activity ProductDetail
     */
    private void initData() {
        product = (Product) getIntent().getSerializableExtra("data");
        activityProductDetailBinding.textViewNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        activityProductDetailBinding.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(String.valueOf(product.getPrice()))) + "₫");
        Picasso.get().load(product.getPicture()).into(activityProductDetailBinding.imageViewPictureProduct);
        activityProductDetailBinding.textViewDetailProduct.setText(product.getDescription());
        activityProductDetailBinding.textViewQuantityProductDetail.setText(String.valueOf(product.getQuantity()));

        /* Khoi tao spinner */
        Integer[] numberProduct = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numberProduct);
        activityProductDetailBinding.spinnerProductDetailScreen.setAdapter(adapterSpinner);

        /* Cap nhat so luong loai san pham trong gio hang moi khi hien thi ProductDetailActivity */
        MainActivity.getCartDetail();
    }

    public void setUpActionBar() {
        setSupportActionBar(activityProductDetailBinding.toolBarProductDetailScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // setDisplayHomeAsUpEnabled() de cho phep kich hoat se quay lai activity truoc khi chon Up
        activityProductDetailBinding.toolBarProductDetailScreen.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Xu ly su kien cap nhat lai so luong san pham trong gio hang
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNtfCountEvent(NtfCountEvent event) {
        if (event != null) {
            if (ApiUtils.listCart.size() != 0) {
                activityProductDetailBinding.ntfCount.setText(String.valueOf(ApiUtils.listCart.size()));
            } else {
                activityProductDetailBinding.ntfCount.clear();
            }
        }
    }
}