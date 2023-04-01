package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.CartAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {
    private CartAdapter cartAdapter;

    private ActivityCartBinding activityCartBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            this.getCart();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Do du lieu len recyclerView
     */
    private void getCart() {
        if (MainActivity.listCart.size() == 0) { // neu gio hang trong
            activityCartBinding.textViewCartEmpty.setVisibility(View.VISIBLE);
        } else {
            activityCartBinding.linearLayoutCartScreen.setVisibility(View.VISIBLE); // khi co du lieu thi moi hien thi no thi se truc quan hon
            activityCartBinding.buttonBuy.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(getApplicationContext(), R.layout.item_cart, MainActivity.listCart);
            activityCartBinding.recyclerViewCartScreen.setAdapter(cartAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            activityCartBinding.recyclerViewCartScreen.setLayoutManager(layoutManager);
            activityCartBinding.recyclerViewCartScreen.addItemDecoration(new ItemDecoration(15));
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityCartBinding.toolBarCartScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityCartBinding.toolBarCartScreen.setNavigationOnClickListener(v -> finish());
    }
}