package com.hoanglinhsama.ecommerce.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivitySignUpBinding;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        if (MainActivity.isConnected(getApplicationContext())) {
            this.getEventSignUp();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventSignUp() {
        activitySignUpBinding.buttonSignupScreen.setOnClickListener(v -> {
            String email = activitySignUpBinding.editTextEmailSignupScreen.getText().toString().trim();
            String password = activitySignUpBinding.editTextPasswordSignupScreen.getText().toString().trim();
            String rePassword = activitySignUpBinding.editTextRepasswordSignupScreen.getText().toString().trim();
            String name = activitySignUpBinding.editTextNameSignupScreen.getText().toString().trim();
            String phoneNumber = activitySignUpBinding.editTextPhoneNumberSignupScreen.getText().toString().trim();
            int type;
            if (activitySignUpBinding.checkboxSignupScreen.isChecked()) {
                type = 1;
            } else {
                type = 2;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Chưa nhập email !", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Chưa nhập mật khẩu !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(rePassword)) {
                    Toast.makeText(this, "Chưa nhập lại mật khẩu !", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(rePassword)) {
                    Toast.makeText(this, "Nhập lại mật khẩu không trùng khớp !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "Chưa nhập họ và tên !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "Chưa nhập số điện thoại !", Toast.LENGTH_SHORT).show();
                } else {
                    DataClient dataClient = ApiUtils.getData();
                    Call<String> call = dataClient.signUp(email, password, name, phoneNumber, type);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                if (response.body().equals("Email already exists !")) {
                                    Toast.makeText(SignUpActivity.this, "Email đã tồn tại !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("getEventSignUp", t.getMessage());
                        }
                    });
                }
            }
        });
    }
}