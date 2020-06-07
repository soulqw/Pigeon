package com.qw.sample;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cd5160866
 */
public class Pigeon {

    private static volatile Pigeon instance;

    private MutableLiveData liveData;

    //    private Map<Object, Method> methodMap = new ArrayMap<>();
    private Set<MethodSubscribe> allSubs = new HashSet<>();

    private Pigeon() {
        liveData = new MutableLiveData();
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
        if (any instanceof LifecycleOwner) {
            liveData.observe((LifecycleOwner) any, new Observer() {
                @Override
                public void onChanged(Object o) {
                    for (MethodSubscribe sub : allSubs) {
                        if (o.getClass() == sub.getArgsType()) {
                            try {
                                sub.getMethod().invoke(any, o);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            });
        }
        bindMethods(any);
    }

    private void bindMethods(Object obj) {
        Class clz = obj.getClass();
        Method[] allMethod = clz.getDeclaredMethods();
        for (Method method : allMethod) {
            Annotation annotation = method.getAnnotation(Subscribe.class);
            if (null != annotation &&
                    method.getParameterTypes().length == 1) {
                Class type = method.getParameterTypes()[0];
                MethodSubscribe methodSubscribe = new MethodSubscribe(obj, method, type);
                allSubs.add(methodSubscribe);
            }
        }
    }

    public void unRegister(Object object) {

    }

    public void post(Object object) {
        if (null == liveData) {
            return;
        }
        liveData.setValue(object);
    }

}
