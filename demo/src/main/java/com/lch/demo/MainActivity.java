package com.lch.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lch.route.RouteManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RouteManager.service("myapp://login")
                .methodName("login")
                .argTypes(String.class, String.class)
                .args("lisi", "123456")
                .invoke();
    }
}
