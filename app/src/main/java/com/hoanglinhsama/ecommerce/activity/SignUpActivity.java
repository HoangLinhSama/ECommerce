package com.hoanglinhsama.ecommerce.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hoanglinhsama.ecommerce.R;
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

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            getEventSignUp();
            getEventShowPassword();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(activitySignUpBinding.toolBarSignupScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activitySignUpBinding.toolBarSignupScreen.setNavigationOnClickListener(v -> finish());
    }

    private void getEventShowPassword() {
        activitySignUpBinding.editTextPasswordSignupScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawableLeft = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_password);
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (activitySignUpBinding.editTextPasswordSignupScreen.getRight() - activitySignUpBinding.editTextPasswordSignupScreen.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (activitySignUpBinding.editTextPasswordSignupScreen.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            activitySignUpBinding.editTextPasswordSignupScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_hide);
                            activitySignUpBinding.editTextPasswordSignupScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        } else {
                            activitySignUpBinding.editTextPasswordSignupScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_show);
                            activitySignUpBinding.editTextPasswordSignupScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        activitySignUpBinding.editTextRepasswordSignupScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawableLeft = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_password);
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (activitySignUpBinding.editTextRepasswordSignupScreen.getRight() - activitySignUpBinding.editTextRepasswordSignupScreen.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (activitySignUpBinding.editTextRepasswordSignupScreen.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            activitySignUpBinding.editTextRepasswordSignupScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_hide);
                            activitySignUpBinding.editTextRepasswordSignupScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        } else {
                            activitySignUpBinding.editTextRepasswordSignupScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_show);
                            activitySignUpBinding.editTextRepasswordSignupScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
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

                                    /* Sau khi dang ky thanh cong thi tu dong quay ve trang dang nhap va dien san email va password o muc dang nhap */
                                    ApiUtils.currentUser.setEmail(email);
                                    ApiUtils.currentUser.setPassword(password);
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