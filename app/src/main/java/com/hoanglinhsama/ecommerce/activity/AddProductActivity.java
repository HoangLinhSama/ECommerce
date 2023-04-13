package com.hoanglinhsama.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.databinding.ActivityAddProductBinding;

public class AddProductActivity extends AppCompatActivity {
    private ActivityAddProductBinding activityAddProductBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddProductBinding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(activityAddProductBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();

        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        Integer[] numberProduct = new Integer[]{1, 2};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numberProduct);
        activityAddProductBinding.spinnerAddProductScreen.setAdapter(adapterSpinner);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityAddProductBinding.toolBarAddProductScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityAddProductBinding.toolBarAddProductScreen.setNavigationOnClickListener(v -> finish());
    }
}