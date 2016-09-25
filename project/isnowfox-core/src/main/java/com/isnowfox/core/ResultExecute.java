package com.isnowfox.core;

/**
 * @author zuoge85 on 2014/8/29.
 */
public interface ResultExecute<R, A> {
    R exe(A a);
}
