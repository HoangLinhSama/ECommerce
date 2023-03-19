package com.hoanglinhsama.ecommerce;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hoanglinhsama.ecommerce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }
}