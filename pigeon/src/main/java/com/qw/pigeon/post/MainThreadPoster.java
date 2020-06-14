package com.qw.pigeon.post;

import android.os.Handler;
import android.os.Looper;

import com.qw.pigeon.MethodSubscribe;

import java.util.LinkedList;

/**
 * @author cd5160866
 * @date 2020/6/14
 */
public class MainThreadPoster implements Poster, Runnable {

    private Handler handler = new Handler(Looper.getMainLooper());

    private LinkedList<PendingPost> mainPosts = new LinkedList<>();

    @Override
    public void post(Object obj, MethodSubscribe subscribe) {
        synchronized (this) {
            mainPosts.offer(PendingPost.obtainPendingPost(obj, subscribe));
            handler.post(this);
        }
    }

    @Override
    public void run() {
        PendingPost pendingPost = mainPosts.poll();
        if (null != pendingPost) {
            pendingPost.invoke();
        }
    }

}
