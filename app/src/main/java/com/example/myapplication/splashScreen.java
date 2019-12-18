package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class splashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this,Login.class));
    }

    @Override
    public void onBackPressed()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
