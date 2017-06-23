package com.babytree.demo;

import android.util.Log;

import com.lch.route.RouteServiceAnnotation;

/**
 * Created by Administrator on 2017/6/23.
 */
@RouteServiceAnnotation(serviceInterface = "myapp://login")
public class LoginModuleService {

    public void login(String name, String pwd) {
        Log.e("test", name + "," + pwd);
    }
}
