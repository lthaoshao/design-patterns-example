package com.lthaoshao.pattern.singleton.threadlocal;

import com.lthaoshao.pattern.singleton.ConcurrentExecutor;

/**
 * <p> 在同一个线程内,无论并发多少, 都是一样的 </p>
 *
 * @author lijinghao
 * @version : ThreadLocalSingletonTest.java, v 0.1 2019年07月18日 16:52:52 lijinghao Exp $
 */
public class ThreadLocalSingletonTest {

    public static void main(String[] args) {

        try {
            ConcurrentExecutor.execute(() -> {
                ThreadLocalSingleton instance = ThreadLocalSingleton.getInstance();
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " : " + instance);

            }, 30, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
