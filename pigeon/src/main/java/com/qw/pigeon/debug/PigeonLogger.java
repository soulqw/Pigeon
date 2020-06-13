package com.qw.pigeon.debug;

import android.util.Log;

/**
 * @author cd5160866
 * @date 2020/6/13
 */
public class PigeonLogger {

    public static final String TAG = PigeonLogger.class.getSimpleName();

    private static boolean isDebug = false;

    public static void setDebug(boolean isDebug) {
        PigeonLogger.isDebug = isDebug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

}
