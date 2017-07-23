package com.lch.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lch.route.RouteManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            RouteManager.service("mt")
                    .methodName("login")
                    .args("lisi", "123456")
                    .invokeDirect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
          //  boolean isRegisterSuccess = RouteManager.route("myapp://com.lch/mt/register?name=lich&age=100");

            test("1", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void hookXM(Object o1, Object o2) {
        Log.e("lich", "shit-------------");
    }

    public static void test(Object o1, Object o2) {
        Log.e("lich", "test-------------");
    }
}
