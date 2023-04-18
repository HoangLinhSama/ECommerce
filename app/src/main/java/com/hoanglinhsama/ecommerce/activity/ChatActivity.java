package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoanglinhsama.ecommerce.ItemDecoration;
import com.hoanglinhsama.ecommerce.adapter.ChatAdapter;
import com.hoanglinhsama.ecommerce.databinding.ActivityChatBinding;
import com.hoanglinhsama.ecommerce.model.ChatMessage;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding activityChatBinding;
    private FirebaseFirestore database;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> listMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        setUpActionBar();
        initData();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventSendMessage();
            listenerMessage();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        database = FirebaseFirestore.getInstance();
        listMessage = new ArrayList<>();
        chatAdapter = new ChatAdapter(getApplicationContext(), listMessage, String.valueOf(ApiUtils.currentUser.getId()));
        activityChatBinding.recyclerViewChatScreen.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        activityChatBinding.recyclerViewChatScreen.setLayoutManager(layoutManager);
        activityChatBinding.recyclerViewChatScreen.addItemDecoration(new ItemDecoration(10));
    }

    private void getEventSendMessage() {
        activityChatBinding.imageViewSendMessage.setOnClickListener(v -> {
            sendMessageToFireStore();
        });
    }

    private void sendMessageToFireStore() {
        if (TextUtils.isEmpty(activityChatBinding.editTextContentMessage.getText().toString().trim())) {

        } else {
            /* Chu y : neu la kieu du lieu nguyen thuy thi phai put vao kieu String vi khi lay du lieu se dung getString() :(( */
            HashMap<String, Object> message = new HashMap<>();
            message.put(ApiUtils.KEY_SEND, String.valueOf(ApiUtils.currentUser.getId()));
            message.put(ApiUtils.KEY_RECEIVE, ApiUtils.receiveId);
            message.put(ApiUtils.KEY_MESSAGE, activityChatBinding.editTextContentMessage.getText().toString().trim());
            message.put(ApiUtils.KEY_DATE_TIME, new Date());
            database.collection(ApiUtils.PATH_CHAT).add(message);

            activityChatBinding.editTextContentMessage.setText(""); // gui tin nhan xong thi xoa noi dung nhap vao, de tiep tuc nhan
        }
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->
    {
        if (error != null) { // co loi xay ra
            return;
        }
        if (value != null) // co gia tri
        {
            int count = listMessage.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSendId(documentChange.getDocument().getString(ApiUtils.KEY_SEND));
                    chatMessage.setReceiveId(documentChange.getDocument().getString(ApiUtils.KEY_RECEIVE));
                    chatMessage.setContentMessage(documentChange.getDocument().getString(ApiUtils.KEY_MESSAGE));
                    chatMessage.setDateObject(documentChange.getDocument().getDate(ApiUtils.KEY_DATE_TIME));
                    chatMessage.setDateTime(formatDateTime(documentChange.getDocument().getDate(ApiUtils.KEY_DATE_TIME)));
                    listMessage.add(chatMessage);
                }
            }
            Collections.sort(listMessage, Comparator.comparing(ChatMessage::getDateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyDataSetChanged();
                activityChatBinding.recyclerViewChatScreen.smoothScrollToPosition(listMessage.size() - 1);
            }
        }
    };

    private void listenerMessage() {
        database.collection(ApiUtils.PATH_CHAT)
                .whereEqualTo(ApiUtils.KEY_SEND, String.valueOf(ApiUtils.currentUser.getId()))
                .whereEqualTo(ApiUtils.KEY_RECEIVE, ApiUtils.receiveId)
                .addSnapshotListener(eventListener);

        database.collection(ApiUtils.PATH_CHAT)
                .whereEqualTo(ApiUtils.KEY_SEND, ApiUtils.receiveId)
                .whereEqualTo(ApiUtils.KEY_RECEIVE, String.valueOf(ApiUtils.currentUser.getId()))
                .addSnapshotListener(eventListener);
    }

    private String formatDateTime(Date dateTime) {
        return new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(dateTime);
    }

    private void setUpActionBar() {
        setSupportActionBar(activityChatBinding.toolBarChatScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityChatBinding.toolBarChatScreen.setNavigationOnClickListener(v -> finish());
    }
}