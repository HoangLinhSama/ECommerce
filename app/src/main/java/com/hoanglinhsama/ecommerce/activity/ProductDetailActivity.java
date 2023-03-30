package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.databinding.ActivityProductDetailBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding activityProductDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(activityProductDetailBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            this.initData();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Nhan du lieu tu intent de hien thi len activity ProductDetail
     */
    private void initData() {
        Product product = (Product) getIntent().getSerializableExtra("data");
        activityProductDetailBinding.textViewNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        activityProductDetailBinding.textViewPriceProduct.setText(decimalFormat.format(Double.parseDouble(product.getPrice())) + "₫");
        Picasso.get().load(product.getPicture()).into(activityProductDetailBinding.imageViewPictureProduct);
        activityProductDetailBinding.textViewDetailProduct.setText(product.getDescription());

        /* Khoi tao spinner */
        Integer[] numberProduct = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numberProduct);
        activityProductDetailBinding.spinnerProductDetailScreen.setAdapter(adapterSpinner);

    }

    public void setUpActionBar() {
        setSupportActionBar(activityProductDetailBinding.toolBarProductDetailScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityProductDetailBinding.toolBarProductDetailScreen.setNavigationOnClickListener(v -> finish());
    }
}