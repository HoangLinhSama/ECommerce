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
import com.hoanglinhsama.ecommerce.eventbus.NotifyDataEvent;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void totalMoney() {
        AtomicLong totalMoney = new AtomicLong();
        MainActivity.listCart.forEach(cart -> {
            totalMoney.set(totalMoney.get() + cart.getTotalPrice());
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        activityCartBinding.textViewTotalPrice.setText(decimalFormat.format((totalMoney.get())) + "₫");
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
            cartAdapter = new CartAdapter(getApplicationContext(), R.layout.item_cart);
            activityCartBinding.recyclerViewCartScreen.setAdapter(cartAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            activityCartBinding.recyclerViewCartScreen.setLayoutManager(layoutManager);
            activityCartBinding.recyclerViewCartScreen.addItemDecoration(new ItemDecoration(15));
            this.totalMoney();
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityCartBinding.toolBarCartScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityCartBinding.toolBarCartScreen.setNavigationOnClickListener(v -> finish());
    }

    @Override
    /*
     * Dang ky nhan event do event bus lam trung gian
     */
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Huy dang ky nhan event
     */
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Xu ly su kien
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    // dinh nghia luong ma method nay se duoc goi boi EventBus
    public void onNotifyDataEvent(NotifyDataEvent event) {
        if (event != null) {
            totalMoney();
            cartAdapter.notifyDataSetChanged();
        }
    }
}