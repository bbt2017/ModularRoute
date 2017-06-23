# ModularRoute
## 简介
用于实现模块化开发各个模块之间的解耦通信。使各个模块不存在相互的类依赖，直接以path的形式实现相互调用。
## 使用示例
1. 定义一个对外提供的服务
```java
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

```
---
2. 在其他模块对服务进行调用。
```java
RouteManager.service("myapp://login")
                .methodName("login")
                .argTypes(String.class, String.class)
                .args("lisi", "123456")
                .invoke();
```

## 集成方法
root project下的build.gradle添加：
```
buildscript {
    repositories {
        jcenter()
        mavenCentral() 
    }
    dependencies {
        classpath 'com.babytree.route:route-plugin:+'
        ...
    }

    allprojects {
        repositories {
            jcenter()

            mavenCentral()
        }
    }
}
```
app的build.gradle添加：
```
dependencies {
   
    compile 'com.babytree.route:route-runtime:+'
   
}

apply plugin: 'com.babytree.route.plugin'
```

备注:目前还未上传到公共maven库。