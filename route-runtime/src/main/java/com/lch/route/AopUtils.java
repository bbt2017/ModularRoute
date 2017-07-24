package com.lch.route;

/**
 * Created by lichenghang on 2017/7/24.
 */

public class AopUtils {

    public static String getClassDesc(String classname) {

        return String.format("L%s;", classname.replaceAll("\\.", "/"));
    }
}
