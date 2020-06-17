package com.qw.pigeon;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.qw.pigeon.debug.PigeonLogger;
import com.qw.pigeon.post.BackGroundPoster;
import com.qw.pigeon.post.MainThreadPoster;
import com.qw.pigeon.post.Poster;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.qw.pigeon.debug.PigeonLogger.TAG;

/**
 * @author cd5160866
 */
public class Pigeon {

    private final ExecutorService executorService;

    private static volatile Pigeon instance;

    private Map<Object, List<MethodSubscribe>> subscriptionsMap;

    private Poster mainThreadPoster;

    private Poster backGroundPoster;

    private Pigeon() {
        subscriptionsMap = new ArrayMap<>(8);
        executorService = Executors.newCachedThreadPool();
        backGroundPoster = new BackGroundPoster(executorService);
        mainThreadPoster = new MainThreadPoster();
    }

    public static Pigeon getDefault() {
        if (instance == null) {
            synchronized (Pigeon.class) {
                if (instance == null) {
                    instance = new Pigeon();
                }
            }
        }
        return instance;
    }

    public void register(final Object any) {
        if (null == any) {
            PigeonLogger.w(TAG, "null object: register");
            return;
        }
        synchronized (this) {
            bindMethods(any);
        }
    }

    public void unRegister(Object any) {
        if (null == any) {
            PigeonLogger.w(TAG, "null object: unRegister");
            return;
        }
        synchronized (this) {
            unBindObject(any);
        }
    }

    public void post(Object object) {
        if (null == object) {
            PigeonLogger.w(TAG, "null object posted");
            return;
        }
        List<MethodSubscribe> typeSubscriptions = subscriptionsMap.get(object.getClass());
        if (null == typeSubscriptions) {
            PigeonLogger.w(TAG, "there is no any subscriptions");
            return;
        }
        for (MethodSubscribe sub : typeSubscriptions) {
            postInternal(sub, object);
        }
    }

    private void postInternal(MethodSubscribe sub, Object object) {
        switch (sub.getThreadModel()) {
            case ThreadMode.ASYNC:
                backGroundPoster.post(object, sub);
                break;
            case ThreadMode.MAIN:
                if (Utils.assertMainThread()) {
                    sub.callSubscribeMethodIfNeeded(object);
                } else {
                    mainThreadPoster.post(object, sub);
                }
                break;
            case ThreadMode.BACKGROUND:
                if (Utils.assertMainThread()) {
                    backGroundPoster.post(object, sub);
                } else {
                    sub.callSubscribeMethodIfNeeded(object);
                }
                break;
            case ThreadMode.POSTING:
                sub.callSubscribeMethodIfNeeded(object);
                break;
            default:
                break;
        }
    }

    private void bindMethods(@NonNull Object obj) {
        Class clz = obj.getClass();
        Method[] allMethod = clz.getDeclaredMethods();
        for (Method method : allMethod) {
            Subscribe subAnnotation = method.getAnnotation(Subscribe.class);
            if (null != subAnnotation &&
                    method.getParameterTypes().length > 0) {

                //get subscribe method list by type
                Object paramsType = method.getParameterTypes()[0];
                List<MethodSubscribe> typeSubscriptions = subscriptionsMap.get(paramsType);
                if (null == typeSubscriptions) {
                    typeSubscriptions = new ArrayList<>();
                    subscriptionsMap.put(paramsType, typeSubscriptions);
                }
                //add to subscribe list if needed
                MethodSubscribe methodSubscribe = new MethodSubscribe(obj, method, subAnnotation.threadMode());
                if (!typeSubscriptions.contains(methodSubscribe)) {
                    typeSubscriptions.add(methodSubscribe);
                }

            }
        }
    }

    private void unBindObject(@NonNull Object obj) {

        Set<Map.Entry<Object, List<MethodSubscribe>>> allSet = subscriptionsMap.entrySet();
        for (Map.Entry<Object, List<MethodSubscribe>> objectListEntry : allSet) {

            List<MethodSubscribe> typeSubscriptions = objectListEntry.getValue();
            Iterator<MethodSubscribe> methodSubscribeIterator = typeSubscriptions.iterator();
            while (methodSubscribeIterator.hasNext()) {
                MethodSubscribe methodSubscribe = methodSubscribeIterator.next();
                if (methodSubscribe.getHost() == obj) {
                    methodSubscribeIterator.remove();
                }
            }
        }
    }

}
