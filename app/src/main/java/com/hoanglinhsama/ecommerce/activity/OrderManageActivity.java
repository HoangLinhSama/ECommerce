package com.hoanglinhsama.ecommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.AdminFeatureAdapter;
import com.hoanglinhsama.ecommerce.adapter.OrderHistoryAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityOrderManageBinding;
import com.hoanglinhsama.ecommerce.eventbus.UpdateStatusOrderEvent;
import com.hoanglinhsama.ecommerce.model.AdminFeature;
import com.hoanglinhsama.ecommerce.model.NotificationReceiveData;
import com.hoanglinhsama.ecommerce.model.NotificationSendData;
import com.hoanglinhsama.ecommerce.model.Order;
import com.hoanglinhsama.ecommerce.model.ProductOrder;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;
import com.hoanglinhsama.ecommerce.retrofit2.DataPushNotification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Quan ly cac don hang da dat cua user
 */
public class OrderManageActivity extends AppCompatActivity {
    private ActivityOrderManageBinding activityOrderManageBinding;
    private List<AdminFeature> listAdminFeature;
    private AdminFeatureAdapter adminFeatureAdapter;
    private List<Order> listOrder;
    private OrderHistoryAdapter orderHistoryAdapter;
    private Order order;
    private Spinner spinnerUpdateStatusOrder;
    private Button buttonUpdateStatusOrder;
    private Button buttonCacelUpdateStatusOrder;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderManageBinding = ActivityOrderManageBinding.inflate(getLayoutInflater());
        setContentView(activityOrderManageBinding.getRoot());

        setUpActionBar();
        getAdminFeature();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventClickNavigationDrawerMenu();
            getOrderHistory();
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

    /**
     * Lay ra lich su tat ca don hang cua khach hang da dat
     */
    private void getOrderHistory() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<Order>> call = dataClient.getOrderHistory(0); // admin muon lay toan bo don hang cua cac user da dat
        {
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful()) {
                        listOrder = response.body();
                        orderHistoryAdapter = new OrderHistoryAdapter(listOrder, R.layout.item_order_history, getApplicationContext());
                        activityOrderManageBinding.recyclerViewOrderManageScreen.setAdapter(orderHistoryAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        activityOrderManageBinding.recyclerViewOrderManageScreen.setLayoutManager(linearLayoutManager);
                        activityOrderManageBinding.recyclerViewOrderManageScreen.addItemDecoration(new ItemDecoration(15));
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Log.d("getOrderHistory", t.getMessage());
                }
            });
        }
    }

    private void getEventClickNavigationDrawerMenu() {
        activityOrderManageBinding.listViewOrderManageScreen.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(OrderManageActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    startActivity(new Intent(OrderManageActivity.this, ProductManageActivity.class));
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(OrderManageActivity.this, OrderManageActivity.class));
                    finish();
                    break;
                case 3:
                    startActivity(new Intent(OrderManageActivity.this, StatisticalActivity.class));
                    finish();
                    break;
            }
        });
    }

    private void getAdminFeature() {
        listAdminFeature = new ArrayList<>();
        listAdminFeature.add(new AdminFeature("Trang Chủ", R.drawable.ic_home_page));
        listAdminFeature.add(new AdminFeature("Quản lý sản phẩm", R.drawable.ic_product_manage));
        listAdminFeature.add(new AdminFeature("Quản lý đơn hàng", R.drawable.ic_order_manage));
        listAdminFeature.add(new AdminFeature("Thống kê sản phẩm", R.drawable.ic_statistical));
        adminFeatureAdapter = new AdminFeatureAdapter(getApplicationContext(), R.layout.item_admin_feature, listAdminFeature);
        activityOrderManageBinding.listViewOrderManageScreen.setAdapter(adminFeatureAdapter);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityOrderManageBinding.toolBarOrderManageScreen);
        activityOrderManageBinding.toolBarOrderManageScreen.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        activityOrderManageBinding.toolBarOrderManageScreen.setNavigationOnClickListener(v -> activityOrderManageBinding.drawerLayoutOrderManageScreen.openDrawer(GravityCompat.START));
    }

    /**
     * Xu ly event cap nhat status cho don hang do OrderHistoryAdapter gui den OrderManageActivity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateStatusOrder(UpdateStatusOrderEvent event) {
        if (event != null) {
            order = event.getOrder();
            /* Khoi tao dialog */
            Dialog dialogUpdateStatusOrder = new Dialog(this);
            dialogUpdateStatusOrder.setContentView(R.layout.dialog_update_status_order);
            dialogUpdateStatusOrder.setCanceledOnTouchOutside(false);
            spinnerUpdateStatusOrder = dialogUpdateStatusOrder.findViewById(R.id.spinner_update_status_order);
            buttonUpdateStatusOrder = dialogUpdateStatusOrder.findViewById(R.id.button_update_status_order);
            buttonCacelUpdateStatusOrder = dialogUpdateStatusOrder.findViewById(R.id.button_cancel_update_status_order);
            dialogUpdateStatusOrder.show();

            /* Khoi tao Spinner */
            List<String> listStatus = new ArrayList<>();
            listStatus.add("Đơn hàng đang được xử lý");
            listStatus.add("Đơn hàng đã được chấp nhận");
            listStatus.add("Đơn hàng đã được giao cho đơn vị vận chuyển");
            listStatus.add("Đơn hàng đã giao thành công");
            listStatus.add("Đơn hàng đã bị hủy");
            ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listStatus);
            spinnerUpdateStatusOrder.setAdapter(arrayAdapterStatus);
            spinnerUpdateStatusOrder.setSelection(order.getStatus());
            spinnerUpdateStatusOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    status = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            /* Cap nhat trang thai don hang len database */
            buttonUpdateStatusOrder.setOnClickListener(v -> {
                DataClient dataClient = ApiUtils.getData();
                Call<String> call = dataClient.updateStatusOrder(order.getId(), status);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            dialogUpdateStatusOrder.cancel();
                            if (status == 4) // neu don hang bi huy
                            {
                                updateQuantityProduct(order.getListProductOrder()); // hoan lai so luong cua moi san pham trong don hang da dat
                            }
                            pushNotificationToUser();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("onUpdateStatusOrder", t.getMessage());
                    }
                });
            });
            buttonCacelUpdateStatusOrder.setOnClickListener(v -> {
                dialogUpdateStatusOrder.cancel();
            });
        }
    }

    /**
     * Hoan lai so luong cua moi loai san pham trong don hang neu don hang bi huy
     */
    private void updateQuantityProduct(List<ProductOrder> listProductOrder) {
        DataClient dataClient = ApiUtils.getData();
        listProductOrder.forEach(product -> {
            Call<String> call = dataClient.updateQuantityProduct(product.getIdProduct(), product.getQuantityRemain() + product.getQuantity());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("updateQuantityProduct", t.getMessage());
                }
            });
        });
    }

    /**
     * Gui thong bao den user ve trang thai don hang da cap nhat
     */
    private void pushNotificationToUser() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<User>> call = dataClient.getTokenUser(order.getId());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    String tokenUser = response.body().get(0).getToken(); // moi don hang chi thuoc 1 user
                    Map<String, String> notification = new HashMap<>();
                    notification.put("title", "Trạng thái đơn hàng");
                    notification.put("body", OrderHistoryAdapter.statusOrder(status));
                    NotificationSendData notificationSendData = new NotificationSendData(tokenUser, notification);
                    DataPushNotification dataPushNotification = ApiUtils.getDataNotification();
                    Call<NotificationReceiveData> call1 = dataPushNotification.sendNotification(notificationSendData);
                    call1.enqueue(new Callback<NotificationReceiveData>() {
                        @Override
                        public void onResponse(Call<NotificationReceiveData> call, Response<NotificationReceiveData> response) {
                            if (response.isSuccessful()) {
                                getOrderHistory();
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationReceiveData> call, Throwable t) {
                            Log.d("sendNotification", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("getTokenUser", t.getMessage());
            }
        });
    }
}