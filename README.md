# ModularRoute
## 简介
用于实现模块化开发各个模块之间的解耦通信。使各个模块不存在相互的类依赖，直接以path的形式实现相互调用。
## 使用示例

1. 定义一个对外提供的服务

```java
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

    public boolean register(JSONObject params) {
        Log.e("test", params.optString("name") + "/" + params.optString("pwd"));
        return true;
    }
}


```
---

2.在其他模块对服务进行调用。

```java
//通过服务名和方法名调用，这种方式支持任何类型参数。同步调用支持返回值。
RouteManager.service("com.demo.account")
                    .methodName("login")
                    .args("lisi", "123456")
                    .invoke();
```

```java
//通过path调用。path必须符合规定格式：schema://serviceName/methodName?params={}
//params对应的值为json对象格式。同步调用支持返回值。
boolean isRegisterSuccess=RouteManager.route("myapp://com.demo.account/register?params={'name':'ch','pwd':'123'}");
```


## 集成方法
root project下的build.gradle添加：

```gradle
buildscript {
    repositories {
        jcenter()
        mavenCentral() 
    }
    dependencies {
        classpath 'com.lch.route:route-plugin:+'
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
```gradle
dependencies {
   
    compile 'com.lch.route:route-runtime:+'
   
}

apply plugin: 'com.lch.route.plugin'
```

备注:目前还未上传到公共maven库。


