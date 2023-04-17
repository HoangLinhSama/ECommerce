package com.hoanglinhsama.ecommerce.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.NewProductAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivitySearchBinding;
import com.hoanglinhsama.ecommerce.model.Product;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding activitySearchBinding;
    private List<Product> listSearchProduct;
    private NewProductAdapter searchProductAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(activitySearchBinding.getRoot());
        setUpActionBar();
        if (isConnected(this)) {
            initData();
            getEventSearch();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        listSearchProduct = new ArrayList<>();
        searchProductAdapter = new NewProductAdapter(listSearchProduct, R.layout.item_new_product, getApplicationContext());
        activitySearchBinding.recyclerViewSearchScreen.addItemDecoration(new ItemDecoration(10));
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        activitySearchBinding.recyclerViewSearchScreen.setLayoutManager(layoutManager);
    }

    private void getEventSearch() {
        activitySearchBinding.editTextSearchScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchProduct();
            }
        });
    }

    private void searchProduct() {
        String nameProduct = activitySearchBinding.editTextSearchScreen.getText().toString().trim();
        DataClient dataClient = ApiUtils.getData();
        Call<List<Product>> call = dataClient.searchProduct(nameProduct);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    listSearchProduct = response.body();
                    searchProductAdapter = new NewProductAdapter(listSearchProduct, R.layout.item_new_product, getApplicationContext());
                    activitySearchBinding.recyclerViewSearchScreen.setAdapter(searchProductAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (t.getMessage().equals("Expected BEGIN_ARRAY but was STRING at line 1 column 1 path $") || t.getMessage().equals("End of input at line 1 column 1 path $")) {
                    listSearchProduct.clear();
                    searchProductAdapter.notifyDataSetChanged();
                }
                Log.d("getEventSearch", t.getMessage());
            }
        });
    }

    private void setUpActionBar() {
        setSupportActionBar(activitySearchBinding.toolBarSearchScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activitySearchBinding.toolBarSearchScreen.setNavigationOnClickListener(v -> finish());
    }

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