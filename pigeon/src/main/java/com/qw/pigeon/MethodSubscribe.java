package com.qw.pigeon;

import androidx.annotation.Nullable;

import com.qw.pigeon.debug.PigeonLogger;

import java.lang.reflect.Method;

import static com.qw.pigeon.debug.PigeonLogger.TAG;

/**
 * @author cd5160866
 * @date 2020/6/7
 */
public class MethodSubscribe {

    private Object host;

    private Method method;

    public MethodSubscribe(Object host, Method method) {
        this.host = host;
        this.method = method;
    }

    public void callSubscribeMethodIfNeeded(Object objectForPost) {
        if (objectForPost.getClass() != method.getParameterTypes()[0]) {
            return;
        }
        try {
            method.invoke(host, objectForPost);
        } catch (Exception e) {
            PigeonLogger.e(TAG, e.toString());
        }
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
                this.method.getName().equals(origin.method.getName()) &&
                this.method.getParameterTypes()[0] == origin.method.getParameterTypes()[0];
    }
}
