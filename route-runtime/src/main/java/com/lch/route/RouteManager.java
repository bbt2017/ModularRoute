package com.lch.route;

import java.lang.reflect.Method;

/**
 * Created by lichenghang on 2017/5/28.
 */

public class RouteManager {

    private static final String gDelegatorClassName = "com.lch.route.Route_";

    /**
     * @return may return null,if cannot find service.
     */

    private static <T> T getService(String serviceName) {
        if (serviceName == null) {
            return null;
        }

        try {
            Class<?> delegCls = Class.forName(gDelegatorClassName);
            Method me = delegCls.getDeclaredMethod("getService", String.class);
            me.setAccessible(true);
            return (T) me.invoke(null, serviceName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static Object callMethod(String serviceName, String methodName, Object[] args, Class<?>... parameterTypes) {
        Object sv = getService(serviceName);
        if (sv == null) {
            return null;
        }
        try {
            Method m = sv.getClass().getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m.invoke(sv, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public static MethodCaller service(String serviceName) {
        return new MethodCaller(serviceName);
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

        public MethodCaller argTypes(Class<?>... parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public MethodCaller args(Object... args) {
            this.args = args;
            return this;
        }

        public <T> T invoke() {
            try {
                return (T) callMethod(serviceName, methodName, args, parameterTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
