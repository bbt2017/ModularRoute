package com.lch.route.plugin.util;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Logg {

    private static final String TAG = "[RouteSDK]";

    public static void e(String msg) {
        System.err.println(TAG + ":" + msg);
    }

    public static void i(String msg) {
        System.out.println(TAG + ":" + msg);
    }
}
