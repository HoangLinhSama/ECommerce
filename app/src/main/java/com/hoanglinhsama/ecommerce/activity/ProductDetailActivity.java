package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivityProductDetailBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    public static ActivityProductDetailBinding activityProductDetailBinding;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(activityProductDetailBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            this.initData();
            this.addToCart();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Bat su kien them san pham vao gio hang
     */
    private void addToCart() {
        activityProductDetailBinding.buttonAddToCart.setOnClickListener(v -> {
            AtomicInteger quantity = new AtomicInteger(Integer.parseInt(activityProductDetailBinding.spinnerProductDetailScreen.getSelectedItem().toString()));
            if (quantity.get() > product.getQuantity()) {
                Toast.makeText(this, "Số lượng sản phẩm còn lại không đủ để thêm vào giỏ hàng !", Toast.LENGTH_SHORT).show();
            } else {
                if (MainActivity.listCart.size() > 0) { // da co it nhat 1 san pham trong gio hang
                    AtomicBoolean exist = new AtomicBoolean(false);
                    MainActivity.listCart.forEach(cart -> {
                        if (product.getId() == cart.getIdProduct()) {
                            quantity.set(cart.getQuantity() + quantity.get());
                            if (quantity.get() > product.getQuantity()) {
                                Toast.makeText(this, "Số lượng sản phẩm còn lại không đủ để thêm vào giỏ hàng !", Toast.LENGTH_SHORT).show();
                                exist.set(true);
                            } else {
                                updateProductToCart(quantity.get());
                                exist.set(true);
                            }
                        }
                    });
                    if (exist.get() == false) { // loai san pham moi them vao chua ton tai trong gio hang
                        addNewProductToCart(quantity.get());
                    }
                } else { // gio hang hien dang trong
                    addNewProductToCart(quantity.get());
                }
            }
        });
    }

    /**
     * Cap nhat lai thong tin loai san pham da ton tai trong gio hang
     */
    private void updateProductToCart(int quantity) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.updateCartDetail(MainActivity.userId, product.getId(), quantity);
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

            }
        });
    }

    /**
     * Them loai san pham chua ton tai trong gio hang vao gio hang
     */
    private void addNewProductToCart(int quantity) {
        /* Them du lieu vao bang cart_detail */
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.insertCartDetail(MainActivity.userId, product.getId(), quantity);
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

            }
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

        /* Khoi tao spinner */
        Integer[] numberProduct = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numberProduct);
        activityProductDetailBinding.spinnerProductDetailScreen.setAdapter(adapterSpinner);

        /* Cap nhat so luong loai san pham trong gio hang moi khi hien thi ProductDetailActivity */
        if (MainActivity.listCart != null) {
            MainActivity.getCartDetail();
        }
    }

    public void setUpActionBar() {
        setSupportActionBar(activityProductDetailBinding.toolBarProductDetailScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityProductDetailBinding.toolBarProductDetailScreen.setNavigationOnClickListener(v -> finish());
    }
}