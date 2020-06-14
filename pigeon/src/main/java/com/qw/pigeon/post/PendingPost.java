package com.qw.pigeon.post;

import com.qw.pigeon.MethodSubscribe;


/**
 * @author cd5160866
 * @date 2020/6/14
 */
public class PendingPost {

    Object postObject;

    MethodSubscribe subscribe;

    public PendingPost(Object postObject, MethodSubscribe subscribe) {
        this.postObject = postObject;
        this.subscribe = subscribe;
    }

    static PendingPost obtainPendingPost(Object postObject, MethodSubscribe subscribe) {
        return new PendingPost(postObject, subscribe);
    }

    public void invoke() {
        subscribe.callSubscribeMethodIfNeeded(postObject);
    }

    public Object getPostObject() {
        return postObject;
    }

    public void setPostObject(Object postObject) {
        this.postObject = postObject;
    }

    public MethodSubscribe getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(MethodSubscribe subscribe) {
        this.subscribe = subscribe;
    }
}
