package com.lch.route.plugin.utils

import org.gradle.api.Project

public class Config {
    public static final int TYPE_APP = 1;
    public static final int TYPE_LIB = 2;

    public static Map objMap = [:]
    public static Project project;

    static Map getExt() {
        return objMap
    }

    public static void setProject(Project project) {
        Config.@project = project
    }


}