package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivityResetPasswordBinding;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                DataClient dataClient = ApiUtils.getData();
                Call<String> call = dataClient.resetPassword(activityResetPasswordBinding.editTextResetPassword.getText().toString().trim());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            if (response.body().equals("Check your email and click on the link sent to your email !")) {
                                ApiUtils.isResetPassword = true; // cho biet lan dang nhap tiep theo la lan dang nhap sau khi reset password
                                Toast.makeText(ResetPasswordActivity.this, "Hãy kiểm tra email và nhấp vào đường dẫn được gửi đến email !", Toast.LENGTH_LONG).show();
                                activityResetPasswordBinding.progressBarResetPasswordScreen.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(ResetPasswordActivity.this, LogInActivity.class));
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Email không đúng !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("getEventForgetPasswords", t.getMessage());
                    }
                });
            }
        });
    }
}