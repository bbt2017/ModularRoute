package com.lch.demo;

import android.util.Log;

import com.lch.route.RouteMethod;
import com.lch.route.RouteServiceAnnotation;

import java.util.Map;

/**
 * Created by Administrator on 2017/6/23.
 */
@RouteServiceAnnotation(serviceInterface = "mt")
public class AccountModuleService {


    public void login(String name, String pwd) {
        Log.e("test", name + "," + pwd);
    }

    @RouteMethod("register")
    public boolean registerImpl(Map<String, String> params) {
        Log.e("test", params.get("name") + "/" + params.get("age"));
        return true;
    }
}
