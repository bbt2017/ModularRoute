# ModularRoute
## 简介
用于实现模块化开发各个模块之间的解耦通信。使各个模块不存在相互的类依赖，直接以path的形式实现相互调用。
## 使用示例

1. 定义一个对外提供的服务

```java

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

```
---

2.在其他模块对服务进行调用。

```java
//通过服务名和方法名调用，这种方式支持任何类型参数。同步调用支持返回值。
RouteManager.service("mt")
                    .methodName("login")
                    .args("lisi", "123456")
                    .invokeDirect();
```

```java
//通过path调用。path必须符合规定格式：schema://host/serviceName/methodName?name=xx&age=12
//params对应的值为json对象格式。同步调用支持返回值。
boolean isRegisterSuccess = RouteManager.route("myapp://com.lch/mt/register?name=lich&age=100");
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


