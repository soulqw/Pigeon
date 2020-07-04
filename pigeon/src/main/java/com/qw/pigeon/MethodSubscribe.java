package com.qw.pigeon;

import android.util.Log;

import androidx.annotation.Nullable;

import com.qw.pigeon.debug.PigeonLogger;

import java.lang.reflect.Method;

import static com.qw.pigeon.debug.PigeonLogger.TAG;

/**
 * @author cd5160866
 * @date 2020/6/7
 */
public class MethodSubscribe {

    private static final int SHIFT = 25;

    private static final int MODE_MASK = 0x63 << SHIFT;

    private static final int IS_STICKY_FLAG = 0x1 << 5;

    private Object host;

    private Method method;

    private int mFlags = 0x0;

    public MethodSubscribe(Object host, Method method, int threadModel, int priority, boolean isSticky) {
        this.host = host;
        this.method = method;
        int flags = 0x0;
        flags |= threadModel;
        if (isSticky) {
            flags |= IS_STICKY_FLAG;
        }
        flags = flags << SHIFT;
        this.mFlags = (priority & ~MODE_MASK) | flags & MODE_MASK;
        Log.d("qw", mFlags + "");
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
        return mFlags & ~MODE_MASK;
    }

    public boolean isSticky() {
        int flag = mFlags & MODE_MASK;
        return (flag & (IS_STICKY_FLAG << SHIFT)) != 0;
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
        int flag = (mFlags & MODE_MASK) >> SHIFT;
        int stickyFlag = IS_STICKY_FLAG;
        return (flag | stickyFlag) ^ stickyFlag;
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
