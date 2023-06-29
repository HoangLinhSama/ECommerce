package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.adapter.ListChatAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityListChatBinding;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Hien thi danh sach cac user duoc phep nhan tin voi admin
 */
public class ListChatActivity extends AppCompatActivity {
    private ActivityListChatBinding activityListChatBinding;
    private List<User> listUser;
    private ListChatAdapter listChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListChatBinding = ActivityListChatBinding.inflate(getLayoutInflater());
        setContentView(activityListChatBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            getListUserFromFireStore();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Lay danh sach user tu cloud firestore database
     */
    private void getListUserFromFireStore() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(ApiUtils.PATH_USER)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            User user = new User();
                            user.setName(queryDocumentSnapshot.getString("userName"));
                            user.setId(Integer.parseInt(queryDocumentSnapshot.getString("id")));
                            listUser.add(user);
                        }
                        if (listUser.size() > 0) {
                            listChatAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initData() {
        listUser = new ArrayList<>();
        listChatAdapter = new ListChatAdapter(getApplicationContext(), R.layout.item_list_chat, listUser);
        activityListChatBinding.recyclerViewListChatScreen.setAdapter(listChatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        activityListChatBinding.recyclerViewListChatScreen.setLayoutManager(layoutManager);
        activityListChatBinding.recyclerViewListChatScreen.addItemDecoration(new ItemDecoration(20));
    }

    private void setUpActionBar() {
        setSupportActionBar(activityListChatBinding.toolBarListChatScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityListChatBinding.toolBarListChatScreen.setNavigationOnClickListener(v -> finish());
    }
}