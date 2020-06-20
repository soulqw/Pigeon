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

    private static final int IS_STICKY_FLAG = 0x1 << 5;

    private Object host;

    private Method method;

    private int priority;

    private int mFlags;

    public MethodSubscribe(Object host, Method method, int threadModel, int priority, boolean isSticky) {
        this.host = host;
        this.method = method;
        this.priority = priority;
        this.mFlags |= threadModel;
        if (isSticky) {
            this.mFlags |= IS_STICKY_FLAG;
        }
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

    public int getPriority() {
        return priority;
    }

    public boolean isSticky() {
        return (mFlags & IS_STICKY_FLAG) != 0;
    }

    public Object getHost() {
        return host;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getThreadModel() {
        return (mFlags | IS_STICKY_FLAG) ^ IS_STICKY_FLAG;
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
                this.getThreadModel() == origin.getThreadModel() &&
                this.method.getParameterTypes()[0] == origin.method.getParameterTypes()[0];
    }
}
