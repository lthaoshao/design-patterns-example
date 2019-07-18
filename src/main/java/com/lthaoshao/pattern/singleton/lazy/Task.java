package com.lthaoshao.pattern.singleton.lazy;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : Task.java, v 0.1 2019年07月16日 16:51:51 lijinghao Exp $
 */
public class Task implements Runnable{

    @Override
    public void run() {
        // InnerClassLazySingleton instance = InnerClassLazySingleton.getInstance();
        DoubleCheckLazySingleton instance = DoubleCheckLazySingleton.getInstance();
        // SimpleLazySingleton instance = SimpleLazySingleton.getInstance();
        System.out.println(instance);
    }
}
