package com.lthaoshao.pattern.singleton.threadlocal;

/**
 * <p> 在单一线程内 保持 单例 </p>
 *
 * @author lijinghao
 * @version : ThreadLocalSingleton.java, v 0.1 2019年07月18日 16:26:26 lijinghao Exp $
 */
public class ThreadLocalSingleton {

    private ThreadLocalSingleton() {
    }

    private static final ThreadLocal<ThreadLocalSingleton> INSTANCE = ThreadLocal.withInitial(ThreadLocalSingleton::new);

    public static ThreadLocalSingleton getInstance(){
        return INSTANCE.get();
    }
}
