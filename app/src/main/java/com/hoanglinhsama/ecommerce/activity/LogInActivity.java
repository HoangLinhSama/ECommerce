package com.hoanglinhsama.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hoanglinhsama.ecommerce.R;
import com.hoanglinhsama.ecommerce.databinding.ActivityLogInBinding;
import com.hoanglinhsama.ecommerce.eventbus.LogOutEvent;
import com.hoanglinhsama.ecommerce.model.User;
import com.hoanglinhsama.ecommerce.retrofit2.ApiUtils;
import com.hoanglinhsama.ecommerce.retrofit2.DataClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding activityLogInBinding;
    private SharedPreferences sharedPreferences;
    public static boolean autoLogin = false; // kiem tra trang thai co cho phep tu dong dang nhap khong (neu lan dau dang nhap thanh cong, thi cac lan sau se duoc tu dong dang nhap)

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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }



    @Override
    protected void onResume() { // Sau khi dang ky thanh cong thi se quay ve trang dang nhap
        super.onResume();
        if (ApiUtils.currentUser.getEmail() != null && ApiUtils.currentUser.getPassword() != null) {
            activityLogInBinding.editTextEmailLoginScreen.setText(ApiUtils.currentUser.getEmail());
            activityLogInBinding.editTextPasswordLoginScreen.setText(ApiUtils.currentUser.getPassword());
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        activityLogInBinding.editTextEmailLoginScreen.setText(sharedPreferences.getString("email", ""));
        activityLogInBinding.editTextPasswordLoginScreen.setText(sharedPreferences.getString("password", ""));
        boolean flag = sharedPreferences.getBoolean("isLogin", false);

        /* Auto login sau 0.5s*/
        if (flag) {
            new Handler().postDelayed(() -> {
                logIn();
            }, 500);
        }
    }

    private void logIn() {
        DataClient dataClient = ApiUtils.getData();
        Call<List<User>> call = dataClient.logIn(activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim(), activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    autoLogin = true;
                    ApiUtils.currentUser = response.body().get(0);

                    /* Luu thong tin dang nhap */
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", activityLogInBinding.editTextEmailLoginScreen.getText().toString().trim());
                    editor.putString("password", activityLogInBinding.editTextPasswordLoginScreen.getText().toString().trim());
                    editor.putBoolean("isLogin", autoLogin);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LogInActivity.this, "Email hoặc mật khẩu không đúng, đăng nhập không thành công !", Toast.LENGTH_SHORT).show();
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
                logIn();
            }
        });
    }

    private void getEventSignUp() {
        activityLogInBinding.textViewSignupLoginScreen.setOnClickListener(v -> {
            startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
        });
    }

    /**
     * Xy ly su kien khi log out, se thay doi gia tri cua autoLogin
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogOutEvent(LogOutEvent event) {
        if (event != null) {
            autoLogin = false;
        }
    }
}