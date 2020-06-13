package com.qw.pigeon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cd5160866
 */
public class Pigeon {

    private static volatile Pigeon instance;

    private List<MethodSubscribe> allSubs;

    private Pigeon() {
        allSubs = new ArrayList<>();
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
            return;
        }
        bindMethods(any);
    }

    public void unRegister(Object any) {
        if (null == any) {
            return;
        }
        unBindObject(any);
    }

    public void post(Object object) {
        for (MethodSubscribe sub : allSubs) {
            sub.callSubscribeMethodIfNeeded(object);
        }
    }

    private void bindMethods(Object obj) {
        Class clz = obj.getClass();
        Method[] allMethod = clz.getDeclaredMethods();
        for (Method method : allMethod) {
            Annotation annotation = method.getAnnotation(Subscribe.class);
            if (null != annotation &&
                    method.getParameterTypes().length == 1) {
                MethodSubscribe methodSubscribe = new MethodSubscribe(obj, method);
                if (!allSubs.contains(methodSubscribe)) {
                    allSubs.add(methodSubscribe);
                }
            }
        }
    }

    private void unBindObject(Object obj) {
        Iterator<MethodSubscribe> methodSubscribeIterator = allSubs.iterator();
        while (methodSubscribeIterator.hasNext()) {
            MethodSubscribe methodSubscribe = methodSubscribeIterator.next();
            if (methodSubscribe.getHost() == obj) {
                methodSubscribeIterator.remove();
            }
        }
    }

}
