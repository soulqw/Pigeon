package com.qw.pigeon;

import android.os.Looper;

import java.util.List;

/**
 * @author cd5160866
 * @date 2020/6/13
 */

class Utils {

    public static boolean isEmpty(List list) {
        return null == list || list.isEmpty();
    }

    public static boolean assertMainThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

}
