package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.CartAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityCartBinding;
import com.hoanglinhsama.ecommerce.eventbus.NotifyChangeOrderEvent;
import com.hoanglinhsama.ecommerce.eventbus.TotalMoneyEvent;
import com.hoanglinhsama.ecommerce.eventbus.DisplayCartEvent;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public class CartActivity extends AppCompatActivity {
    private CartAdapter cartAdapter;
    private ActivityCartBinding activityCartBinding;
    private long price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());

        initData();
        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            getCart();
            getEventOrder();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        ApiUtils.listCartChecked.clear(); // moi lan mo lai ung dung phai clear listCartChecked de no tinh tong tien bat dau lai tu 0
    }

    @Override
    /*
     * Dang ky nhan event do event bus lam trung gian
     */
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.getCartDetail();
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
     * Bat su kien dat hang
     */
    private void getEventOrder() {
        activityCartBinding.buttonBuy.setOnClickListener(v -> {
            if (price != 0) { // neu da checked it nhat mot san pham thi moi cho phep mua hang
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                intent.putExtra("totalMoney", price);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Chưa chọn sản phẩm để mua !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Tinh tong tien cac san pham duoc chon trong gio hang de tien hanh mua hang
     */
    private void totalMoney() {
        AtomicLong totalMoney = new AtomicLong(0);
        ApiUtils.listCartChecked.forEach(cart -> {
            totalMoney.set(totalMoney.get() + cart.getTotalPrice());
        });
        price = totalMoney.get();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        activityCartBinding.textViewTotalPrice.setText(decimalFormat.format((totalMoney.get())) + "₫");
    }

    /**
     * Do du lieu len recyclerView
     */
    private void getCart() {
        if (ApiUtils.listCart.size() == 0) { // neu gio hang trong
            activityCartBinding.textViewCartEmpty.setVisibility(View.VISIBLE);
        } else {
            activityCartBinding.linearLayoutCartScreen.setVisibility(View.VISIBLE); // khi co du lieu thi moi hien thi no thi se truc quan hon
            activityCartBinding.buttonBuy.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(getApplicationContext(), R.layout.item_cart);
            activityCartBinding.recyclerViewCartScreen.setAdapter(cartAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            activityCartBinding.recyclerViewCartScreen.setLayoutManager(layoutManager);
            activityCartBinding.recyclerViewCartScreen.addItemDecoration(new ItemDecoration(15));
            totalMoney(); // ban dau moi vao gio hang thi so tien = 0
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityCartBinding.toolBarCartScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityCartBinding.toolBarCartScreen.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Xu ly su kien tinh lai tong tien cho gio hang
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    // dinh nghia luong ma method nay se duoc goi boi EventBus
    public void onTotalMoneyEvent(TotalMoneyEvent event) {
        if (event != null) {
            totalMoney();
        }
    }

    /**
     * Xu ly su kien cap nhat lai giao dien gio hang khi da xoa het tat ca san pham ra khoi gio hang
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateDisplayCartEvent(DisplayCartEvent event) {
        if (event != null) {
            activityCartBinding.textViewCartEmpty.setVisibility(View.VISIBLE);
            activityCartBinding.linearLayoutCartScreen.setVisibility(View.INVISIBLE); // khi co du lieu thi moi hien thi no thi se truc quan hon
            activityCartBinding.buttonBuy.setVisibility(View.INVISIBLE);

            /* Gio hang trong thi quay lai man hinh chinh sau 1s */
            new Handler().postDelayed(() -> {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }, 1000);

        }
    }

    /**
     * Xu ly su kien cap nhat RecyclerView khi dat hang thanh cong
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyChangeOrder(NotifyChangeOrderEvent event) {
        if (event != null) {
            cartAdapter.notifyDataSetChanged();
        }
    }
}