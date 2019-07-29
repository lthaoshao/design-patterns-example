package com.lthaoshao.pattern.singleton.lazy;

/**
 * <p> 利用了静态内部类的加载特性:
 *  在加载外部类时,静态内部类不会被同时加载, 仅当调用其内部的静态方法或静态字段时, 才会加载静态内部类.
 *  这样的单例实现方式, 既实现了惰性初始化, 也通过JVM层面保证了多线程并发的正确性.
 * </p>
 *
 * @author lijinghao
 * @version : InnerClassLazySingleton.java, v 0.1 2019年07月16日 17:49:49 lijinghao Exp $
 */
public final class InnerClassLazySingleton {

    private InnerClassLazySingleton(){
        // 如果通过反射创建实例, 这里作判断的时候, 会加载内部类,导致INSTANCE不为null, 从而抛出异常.
        // 只能通过getInstance获取实例
        if(SingletonHolder.INSTANCE != null){
            throw new RuntimeException("不可以被实例化");
        }
    }

    static{
        System.out.println("加载外部类");
    }


    public static final InnerClassLazySingleton getInstance(){
        System.out.println("准备获取外部类实例");
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态内部类
     * private是为了不让其他的类访问
     */
    private static class SingletonHolder{
        static{
            System.out.println("加载静态内部类");
        }
        private static final InnerClassLazySingleton INSTANCE = new InnerClassLazySingleton();
    }

}
