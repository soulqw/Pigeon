package com.qw.pigeon;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.qw.pigeon.debug.PigeonLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.qw.pigeon.debug.PigeonLogger.TAG;

/**
 * @author cd5160866
 */
public class Pigeon {

    private static volatile Pigeon instance;

    private Map<Object, List<MethodSubscribe>> subscriptionsMap;

    private Pigeon() {
        subscriptionsMap = new ArrayMap<>(8);
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
        bindMethods(any);
    }

    public void unRegister(Object any) {
        if (null == any) {
            PigeonLogger.w(TAG, "null object: unRegister");
            return;
        }
        unBindObject(any);
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
            sub.callSubscribeMethodIfNeeded(object);
        }
    }

    private void bindMethods(@NonNull Object obj) {
        Class clz = obj.getClass();
        Method[] allMethod = clz.getDeclaredMethods();
        for (Method method : allMethod) {
            Annotation annotation = method.getAnnotation(Subscribe.class);
            if (null != annotation &&
                    method.getParameterTypes().length > 0) {

                //get subscribe method list by type
                Object paramsType = method.getParameterTypes()[0];
                List<MethodSubscribe> typeSubscriptions = subscriptionsMap.get(paramsType);
                if (null == typeSubscriptions) {
                    typeSubscriptions = new ArrayList<>();
                    subscriptionsMap.put(paramsType, typeSubscriptions);
                }
                //add to subscribe list if needed
                MethodSubscribe methodSubscribe = new MethodSubscribe(obj, method);
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
