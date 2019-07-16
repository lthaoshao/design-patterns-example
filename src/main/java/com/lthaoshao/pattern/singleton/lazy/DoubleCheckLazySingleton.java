package com.lthaoshao.pattern.singleton.lazy;

/**
 * <p> 两次检查 </p>
 *
 * @author lijinghao
 * @version : DoubleCheckLazySingleton.java, v 0.1 2019年07月16日 17:31:31 lijinghao Exp $
 */
public class DoubleCheckLazySingleton {

    private DoubleCheckLazySingleton() {
    }

    // volatile 使之内存可见, 禁止指令重排序
    private volatile static DoubleCheckLazySingleton instance;

    public static DoubleCheckLazySingleton getInstance() {
        if (instance == null) {
            // 使得使用synchonized的概率更小了, 效率更高了
            synchronized (DoubleCheckLazySingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckLazySingleton();
                    // 1 为这个对象分配内存
                    // 2 初始化对象
                    // 3 将引用指向分配的内存地址 // 2,3 如果没有volatile,则可能发生重排序问题
                    // 4 初次访问对象
                }
            }
        }

        return instance;
    }
}
