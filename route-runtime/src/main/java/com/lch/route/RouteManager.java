package com.lch.route;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Method;

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

    private static Object callMethod(String serviceName, String methodName, Object[] args, Class<?>... parameterTypes) throws Exception {

        Object sv = getService(serviceName);
        Method m = sv.getClass().getDeclaredMethod(methodName, parameterTypes);
        m.setAccessible(true);

        return m.invoke(sv, args);
    }

    public static MethodCaller service(String serviceName) {
        return new MethodCaller(serviceName);
    }


    public static <T> T route(String url) throws Exception {//schema://serviceName/methodName?params={}

        Uri uri = Uri.parse(url);
        String serviceName = uri.getAuthority();
        String methodName = uri.getLastPathSegment();
        String paramsJson = uri.getQueryParameter("params");
        JSONObject param = new JSONObject(paramsJson);

        Log.e(TAG, "route url=" + url);

        Log.e(TAG, String.format("serviceName=%s,methodName=%s,paramsJson=%s", serviceName, methodName, paramsJson));

        return service(serviceName).methodName(methodName).args(param).invoke();

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

        public <T> T invoke() throws Exception {

            return (T) callMethod(serviceName, methodName, args, parameterTypes);

        }
    }


}
