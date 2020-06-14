package com.qw.pigeon.post;

import com.qw.pigeon.MethodSubscribe;

/**
 * @author cd5160866
 * @date 2020/6/14
 */
public interface Poster {

    /**
     * @param event     the event that posted
     * @param subscribe the subscribe
     */
    void post(Object event, MethodSubscribe subscribe);

}
