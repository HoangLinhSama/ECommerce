package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding activityChatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {

        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityChatBinding.toolBarChatScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityChatBinding.toolBarChatScreen.setNavigationOnClickListener(v -> finish());
    }
}