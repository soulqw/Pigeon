package com.qw.sample;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

/**
 * @author cd5160866
 * @date 2020/6/7
 */
public class MethodSubscribe {

    private Object host;

    private Method method;

    private Class argsType;

    public MethodSubscribe(Object host, Method method, Class argsType) {
        this.host = host;
        this.method = method;
        this.argsType = argsType;
    }

    public Object getHost() {
        return host;
    }

    public void setHost(Object host) {
        this.host = host;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getArgsType() {
        return argsType;
    }

    public void setArgsType(Class argsType) {
        this.argsType = argsType;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (null == obj) {
            return false;
        }
        MethodSubscribe origin = (MethodSubscribe) obj;
        return this.host == origin.host &&
                this.method == origin.method &&
                this.argsType == origin.argsType;
    }
}
