package com.zdf.internalcommon.util;


/**
 *@Description ThreadLocalUtil
 *@Author mrzhang
 *@Date 2024/4/30 23:50
 */
public class ThreadLocalUtil{

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal() {};

    private ThreadLocalUtil(){}

    public static void set(Object data) {
        THREAD_LOCAL.set(data);
    }

    public static Object get() {
        return THREAD_LOCAL.get();
    }
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
