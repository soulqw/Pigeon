package com.qw.sample;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @author cd5160866
 */
public class Pigeon {

    private static volatile Pigeon instance;

    private MutableLiveData liveData;

    private Pigeon() {
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

    public void register(Object object) {
        if (object instanceof LifecycleOwner) {
            liveData.observe((LifecycleOwner) object, new Observer() {
                @Override
                public void onChanged(Object o) {

                }
            });
        }

    }

    public void unRegister(Object object) {

    }

}
