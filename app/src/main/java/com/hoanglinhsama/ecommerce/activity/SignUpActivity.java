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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.databinding.ActivitySignUpBinding;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding activitySignUpBinding;
    private FirebaseAuth firebaseAuth;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        setUpActionBar();
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            getEventSignUp();
            getEventShowPassword();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();

        /* Kiem tra xem da co ai dang ky tai khoan admin chua, neu roi thi chi duoc phep dang ky tai khoan khach hang */
        DataClient dataClient = ApiUtils.getData();
        Call<List<User>> call = dataClient.getTokenAdmin();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) { // da co admin 
                    activitySignUpBinding.textViewTypeUserSignupScreen.setVisibility(View.INVISIBLE);
                    activitySignUpBinding.checkboxSignupScreen.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }

    private void setUpActionBar() {
        setSupportActionBar(activitySignUpBinding.toolBarSignupScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activitySignUpBinding.toolBarSignupScreen.setNavigationOnClickListener(v -> finish());
    }

    private void getEventShowPassword() {
        activitySignUpBinding.editTextPasswordSignupScreen.setOnTouchListener((v, event) -> {
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
        });
        activitySignUpBinding.editTextRepasswordSignupScreen.setOnTouchListener((v, event) -> {
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
        });
    }

    private void getEventSignUp() {
        activitySignUpBinding.buttonSignupScreen.setOnClickListener(v -> {
            ApiUtils.isSignUp = true;
            email = activitySignUpBinding.editTextEmailSignupScreen.getText().toString().trim();
            password = activitySignUpBinding.editTextPasswordSignupScreen.getText().toString().trim();
            String rePassword = activitySignUpBinding.editTextRepasswordSignupScreen.getText().toString().trim();
            name = activitySignUpBinding.editTextNameSignupScreen.getText().toString().trim();
            phoneNumber = activitySignUpBinding.editTextPhoneNumberSignupScreen.getText().toString().trim();
            if (activitySignUpBinding.checkboxSignupScreen.isChecked()) {
                type = 1;
            } else {
                type = 2;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Chưa nhập email !", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
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
                /* Dang ky tai khoan tren firebase Authentication de su dung duoc chuc nang token */
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) { // sau khi dang ky tai khoan tren firebase Authentication thanh cong
                            signUp(email, password, name, phoneNumber, type, firebaseUser.getUid());// dang ky tai khoan tren MySQL (Server)
                        }
                    } else {
                        Toast.makeText(this, "Email đã tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Dang ky user tren MySQL (Server) (de du lieu truc quan hon) sau khi dang ky thanh cong tren Firebase Authentication
     */
    private void signUp(String email, String password, String name, String phoneNumber,
                        int type, String firebaseUId) {
        DataClient dataClient = ApiUtils.getData();
        Call<String> call = dataClient.signUp(email, password, name, phoneNumber, type, firebaseUId); // mac dinh password tren MySQL se hien thi la on firebase
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();

                    /* Sau khi dang ky thanh cong thi tu dong quay ve trang dang nhap va dien san email va password o muc dang nhap */
                    ApiUtils.currentUser.setEmail(email);
                    ApiUtils.currentUser.setPassword(password);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("getEventSignUp", t.getMessage());
            }
        });
    }
}