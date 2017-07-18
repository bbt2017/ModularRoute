package com.lch.route;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lichenghang on 2017/5/28.
 */

public class RouteManager {

    private static final String TAG = "RouteManager";


    private static final String gDelegatorClassName = "com.lch.route.Route_";

    /**
     * @return may return null,if cannot find service.
     */

    private static <T> T getService(String serviceName) throws Exception {

        Class<?> delegCls = Class.forName(gDelegatorClassName);
        Method me = delegCls.getDeclaredMethod("getService", String.class);
        me.setAccessible(true);

        return (T) me.invoke(null, serviceName);

    }

    private static Object callMethodByPath(String serviceName, String methodName, Object[] args) throws Exception {

        Object sv = getService(serviceName);
        Method[] methods = sv.getClass().getDeclaredMethods();
        Method mappedMethod = null;

        for (Method method : methods) {
            RouteMethod anno = method.getAnnotation(RouteMethod.class);
            if (anno == null) {
                continue;
            }
            String routeMethodName = anno.value();
            if (TextUtils.isEmpty(routeMethodName)) {
                continue;
            }

            if (routeMethodName.equals(methodName)) {
                mappedMethod = method;
                break;
            }
        }

        if (mappedMethod == null) {
            throw new IllegalStateException("cannot find mapped method for:" + methodName + ", in service class:" + sv.getClass().getName());
        }

        mappedMethod.setAccessible(true);

        return mappedMethod.invoke(sv, args);
    }

    private static Object callMethodDirect(String serviceName, String methodName, Object[] args, Class<?>... parameterTypes) throws Exception {
        Object sv = getService(serviceName);
        Method m = sv.getClass().getDeclaredMethod(methodName, parameterTypes);
        m.setAccessible(true);

        return m.invoke(sv, args);
    }

    public static MethodCaller service(String serviceName) {
        return new MethodCaller(serviceName);
    }


    public static <T> T route(String url) throws Exception {//schema://host/serviceName/methodName?name=xx&age=12

        Uri uri = Uri.parse(url);
        List<String> segs = uri.getPathSegments();
        if (segs == null || segs.size() != 2) {
            throw new IllegalArgumentException("route url is invalid.");
        }

        String serviceName = segs.get(0);
        String methodName = segs.get(1);

        Set<String> queryNames = uri.getQueryParameterNames();
        Map<String, String> parms = new HashMap<>();
        if (queryNames != null && !queryNames.isEmpty()) {
            for (String key : queryNames) {
                parms.put(key, uri.getQueryParameter(key));
            }
        }

        Log.e(TAG, "route url=" + url);

        Log.e(TAG, String.format("serviceName=%s,methodName=%s,params: %s", serviceName, methodName, uri.getQuery()));

        return service(serviceName).methodName(methodName).args(parms).invokeByPath();

    }


    public static class MethodCaller {

        private String serviceName;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] args;


        public MethodCaller(String serviceName) {
            this.serviceName = serviceName;
        }


        public MethodCaller methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }


        public MethodCaller args(Object... args) {
            this.args = args;

            if (args != null && args.length > 0) {
                this.parameterTypes = new Class<?>[args.length];

                for (int i = 0; i < args.length; i++) {
                    this.parameterTypes[i] = args[i].getClass();
                }

            }

            return this;
        }

        private <T> T invokeByPath() throws Exception {

            return (T) callMethodByPath(serviceName, methodName, args);

        }

        public <T> T invokeDirect() throws Exception {
            return (T) callMethodDirect(serviceName, methodName, args, parameterTypes);
        }
    }


}
