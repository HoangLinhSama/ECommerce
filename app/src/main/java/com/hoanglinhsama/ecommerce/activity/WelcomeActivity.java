package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
            if (sharedPreferences.getString("user", "") != "") { // neu da dang nhap thanh cong tu truoc do thi se vao thang man hinh chinh
                ApiUtils.currentUser = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class); // truong hop khong qua man hinh login thi van lay duoc data cua current user
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, LogInActivity.class));
                finish();
            }
        }, 1000);
    }
}