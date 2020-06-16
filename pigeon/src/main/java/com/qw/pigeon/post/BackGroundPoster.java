package com.qw.pigeon.post;

import com.qw.pigeon.MethodSubscribe;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

/**
 * @author cd5160866
 * @date 2020/6/16
 */
public class BackGroundPoster implements Poster, Runnable {

    private ExecutorService executorService;

    private LinkedList<PendingPost> mainPosts = new LinkedList<>();

    public BackGroundPoster(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void post(Object event, MethodSubscribe subscribe) {
        synchronized (this) {
            mainPosts.offer(PendingPost.obtainPendingPost(event, subscribe));
            executorService.submit(this);
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
