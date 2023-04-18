package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hoanglinhsama.ecommerce.databinding.ActivityChatBinding;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding activityChatBinding;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        setUpActionBar();
        initData();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventSendMessage();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        database = FirebaseFirestore.getInstance();
    }

    private void getEventSendMessage() {
        activityChatBinding.imageViewSendMessage.setOnClickListener(v -> {
            sendMessageToFireStore();
        });
    }

    private void sendMessageToFireStore() {
        if (TextUtils.isEmpty(activityChatBinding.editTextContentMessage.getText().toString().trim())) {

        } else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(ApiUtils.KEY_SEND, ApiUtils.currentUser.getId());
            message.put(ApiUtils.KEY_RECEIVE, ApiUtils.receiveId);
            message.put(ApiUtils.KEY_MESSAGE, activityChatBinding.editTextContentMessage.getText().toString().trim());
            message.put(ApiUtils.KEY_DATE_TIME, new Date());
            database.collection(ApiUtils.PATH_CHAT).add(message);

            activityChatBinding.editTextContentMessage.setText(""); // gui tin nhan xong thi xoa noi dung nhap vao, de tiep tuc nhan
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityChatBinding.toolBarChatScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityChatBinding.toolBarChatScreen.setNavigationOnClickListener(v -> finish());
    }
}