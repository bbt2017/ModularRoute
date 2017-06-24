package com.lch.demo;

import android.util.Log;

import com.lch.route.RouteServiceAnnotation;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/23.
 */
@RouteServiceAnnotation(serviceInterface = "com.demo.account")
public class AccountModuleService {

    public void login(String name, String pwd) {
        Log.e("test", name + "," + pwd);
    }

    public void register(JSONObject params) {
        Log.e("test", params.optString("name") + "/" + params.optString("pwd"));
    }
}
