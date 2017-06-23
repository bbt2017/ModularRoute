package com.lch.route.plugin.utils

import org.gradle.api.Project


public class Util {


    public static Project project;

    public static void setProject(Project project) {
        Util.@project = project
    }




    public static void initTargetClasses(Map<String, Object> modifyMatchMaps) {
        targetClasses.clear()
        if (modifyMatchMaps != null) {
            def set = modifyMatchMaps.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                def value = entry.getValue()
                if (value) {
                    int type;
                    if (value instanceof Map) {
                        type = typeString2Int(value.get(Const.KEY_CLASSMATCHTYPE));
                    } else {
                        type = getMatchTypeByValue(entry.getKey());
                    }
                    targetClasses.put(entry.getKey(), type)
                }
            }
        }
    }


    public static int typeString2Int(String type) {
        if (type == null || Const.VALUE_ALL.equals(type)) {
            return Const.MT_FULL;
        } else if (Const.VALUE_REGEX.equals(type)) {
            return Const.MT_REGEX;
        } else if (Const.VALUE_WILDCARD.equals(type)) {
            return Const.MT_WILDCARD;
        } else {
            return Const.MT_FULL;
        }
    }

    public static int getMatchTypeByValue(String value) {
        if (isEmpty(value)) {
            throw new RuntimeException("Key cannot be null");
        } else if (value.startsWith(Const.REGEX_STARTER)) {
            return Const.MT_REGEX;
        } else if (value.contains("*") || value.contains("|")) {
            return Const.MT_WILDCARD;
        } else {
            return Const.MT_FULL;
        }
    }


    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() < 1;
    }


    public static String path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }


    static Map<String, Integer> targetClasses = [:];


}