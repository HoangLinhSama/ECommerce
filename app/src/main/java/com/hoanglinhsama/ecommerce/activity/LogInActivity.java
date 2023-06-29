package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.databinding.ActivityLogInBinding;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding activityLogInBinding;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogInBinding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(activityLogInBinding.getRoot());
        if (MainActivity.isConnected(getApplicationContext())) {
            initData();
            getEventSignUp();
            getEventLogin();
            getEventShowPassword();
            getEventForgetPassword();
        } else {
            Toast.makeText(this, "Không có Internet ! Hãy kết nối Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() { // Sau khi dang ky thanh cong hoac khi dang xuat thi se quay ve trang dang nhap, va vao state nay
        super.onResume();
        if (ApiUtils.currentUser.getEmail() != null && ApiUtils.currentUser.getPassword() != null) {
            activityLogInBinding.editTextEmailLoginScreen.setText(ApiUtils.currentUser.getEmail());
            if (ApiUtils.isSignUp) { // do dang ky ma tro ve dang nhap
                activityLogInBinding.editTextPasswordLoginScreen.setText(ApiUtils.currentUser.getPassword());
            } else { // do dang xuat ma tro ve dang nhap
                activityLogInBinding.editTextPasswordLoginScreen.setText(sharedPreferences.getString("password", ""));
            }
        }
    }

    private void getEventForgetPassword() {
        activityLogInBinding.textViewForgetPasswordLoginScreen.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, ResetPasswordActivity.class)));
    }

    private void getEventShowPassword() {
        activityLogInBinding.editTextPasswordLoginScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawableLeft = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_password);
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) { // su kien cham
                    if (event.getRawX() >= (activityLogInBinding.editTextPasswordLoginScreen.getRight() - activityLogInBinding.editTextPasswordLoginScreen.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) { // arrayDrawable[]={left, top, right, bottom}, kiem tra event co xay ra tai vi tri cua drawableRight khong
                        if (activityLogInBinding.editTextPasswordLoginScreen.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) { // dang text va da an di
                            activityLogInBinding.editTextPasswordLoginScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // kieu text va duoc show ra
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_hide);
                            activityLogInBinding.editTextPasswordLoginScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        } else {
                            activityLogInBinding.editTextPasswordLoginScreen.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            Drawable drawableRight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_show);
                            activityLogInBinding.editTextPasswordLoginScreen.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initData() {
        /* Lay thong tin trong SharePreferences de phuc vu viec dang nhap */
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        activityLogInBinding.editTextEmailLoginScreen.setText(sharedPreferences.getString("email", ""));
        activityLogInBinding.editTextPasswordLoginScreen.setText(sharedPreferences.getString("password", ""));

        /* Khoi tao firebaseAuthentication va firebaseUsser */
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void logIn() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<User>> call = dataClient.logIn(activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim(), activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) { // dang nhap thanh cong
                    activityLogInBinding.progressBarLoginScreen.setVisibility(View.INVISIBLE);
                    ApiUtils.currentUser = response.body().get(0);

                    /* Luu thong tin dang nhap sau khi dang nhap thanh cong */
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim());
                    editor.putString("password", activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim());
                    editor.putString("user", new Gson().toJson(ApiUtils.currentUser)); // luu thong tin user tra ve khi dang nhap thanh cong, de tu lan dang nhap tiep theo se tu man hinh welcome vao thang man hinh main
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish(); // finish de khi vao man hinh khac ma dieu huong bang back tren he thong android thi se khong tro lai man hinh nay nua
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("getEventLogin", t.getMessage());
            }
        });
    }

    private void getEventLogin() {
        activityLogInBinding.buttonLoginScreen.setOnClickListener(v -> {
            if (TextUtils.isEmpty(activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim())) {
                Toast.makeText(this, "Chưa nhập email !", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim())) {
                Toast.makeText(this, "Chưa nhập mật khẩu !", Toast.LENGTH_SHORT).show();
            } else {
                activityLogInBinding.progressBarLoginScreen.setVisibility(View.VISIBLE);
                if (firebaseUser != null) { // user (dang ky dich vu firebase thanh cong) da dang nhap tren ung dung nay
                    logIn(); // xu ly dang nhap dua tren database MySQL
                } else {
                    firebaseAuth.signInWithEmailAndPassword(activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim(), activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim())
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    logIn(); // Neu dang nhap thanh cong user firebase Authentication thi tiep tuc thuc hien xu ly dang nhap dua tren database MySQL
                                } else {
                                    Toast.makeText(LogInActivity.this, "Email hoặc mật khẩu không đúng, đăng nhập không thành công !", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void getEventSignUp() {
        activityLogInBinding.textViewSignupLoginScreen.setOnClickListener(v -> {
            startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
        });
    }
}