package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hoanglinhsama.ecommerce.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding activityResetPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityResetPasswordBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityResetPasswordBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventForgetPassword();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activityResetPasswordBinding.toolBarResetPasswordScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityResetPasswordBinding.toolBarResetPasswordScreen.setNavigationOnClickListener(v -> finish());
    }

    private void getEventForgetPassword() {
        activityResetPasswordBinding.buttonForgetPassword.setOnClickListener(v -> {
            if (TextUtils.isEmpty(activityResetPasswordBinding.editTextResetPassword.getText().toString().trim())) {
                Toast.makeText(this, "Chưa nhập email !", Toast.LENGTH_SHORT).show();
            } else {
                activityResetPasswordBinding.progressBarResetPasswordScreen.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(activityResetPasswordBinding.editTextResetPassword.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Hãy kiểm tra email và nhấp vào đường dẫn được gửi đến email !", Toast.LENGTH_LONG).show();
                                activityResetPasswordBinding.progressBarResetPasswordScreen.setVisibility(View.INVISIBLE);
                                finish();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Email không đúng !", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}