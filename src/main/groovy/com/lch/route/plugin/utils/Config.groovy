package com.lch.route.plugin.utils

import com.lch.route.plugin.util.Logg
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

import java.lang.reflect.Constructor
import java.lang.reflect.Method

public class Config implements Opcodes{
    public static final int TYPE_APP = 1;
    public static final int TYPE_LIB = 2;

    public static Map objMap = [:]
    public static Project project;

    public static List<ClassVisitor> visitors =[]

    static Map getExt() {
        return objMap
    }

    public static void setProject(Project project) {
        Config.@project = project
    }

    public static void initVisitors(List<String> classpaths){
        visitors.clear()

        URLClassLoader classLoader= (URLClassLoader)Config.class.getClassLoader();//new URLClassLoader(urls);
        Method method_addURL=URLClassLoader.class.getDeclaredMethod("addURL",URL.class)
        method_addURL.setAccessible(true)

        URL[] urls=new URL[classpaths.size()]


        for(int i=0;i<classpaths.size();i++){
            Logg.e(classpaths.get(i))

            method_addURL.invoke(classLoader,new File(classpaths.get(i)).toURL())

            //urls[i]=new File(classpaths.get(i)).toURL()
        }


        List<String> classnames = Config.project.route.visitors;
        for(String classname:classnames){
            Class cls=classLoader.loadClass(classname)
            Constructor ctr=cls.getDeclaredConstructor(int.class)
            ctr.setAccessible(true)


            ClassVisitor v=(ClassVisitor)ctr.newInstance(ASM5)
            visitors.add(v)
        }


    }


}