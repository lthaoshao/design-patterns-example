package com.lthaoshao.pattern.singleton.lazy;

/**
 * <p> 懒汉式: 延迟加载,外部调用时才实例化 </p>
 *
 * @author lijinghao
 * @version : SimpleLazySingleton.java, v 0.1 2019年07月16日 13:46:46 lijinghao Exp $
 */
public class SimpleLazySingleton {

    private SimpleLazySingleton() {
    }

    private static SimpleLazySingleton instance;

    public synchronized static SimpleLazySingleton getInstance() {
        if (instance == null) {
            instance = new SimpleLazySingleton();
        }
        return instance;
    }
}
