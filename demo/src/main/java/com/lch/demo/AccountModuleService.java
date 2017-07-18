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

    /**
     * 使用方法名进行调用。这时可以传任意参数。
     *
     * @param name
     * @param pwd
     */
    public void login(String name, String pwd) {
        Log.e("test", name + "," + pwd);
    }

    /**
     * 使用路由path方式调用必须要加{@code @RouteMethod}注解来说明path中的方法名到服务方法名的映射。
     *
     * @param params 路由path方式调用时参数永远是Map<String, String>
     * @return
     */
    @RouteMethod("register")
    public boolean registerImpl(Map<String, String> params) {
        Log.e("test", params.get("name") + "/" + params.get("age"));
        return true;
    }
}
