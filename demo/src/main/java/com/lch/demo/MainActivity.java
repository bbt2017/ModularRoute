package com.lch.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lch.route.RouteManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            RouteManager.service("com.demo.account")
                    .methodName("login")
                    .args("lisi", "123456")
                    .invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
           boolean isRegisterSuccess= RouteManager.route("myapp://com.demo.account/register?params={'name':'ch','pwd':'123'}");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
