package com.lthaoshao.pattern.singleton.hungry;

/**
 * <p> 单例模式: 饿汉式 </p>
 *
 * @author lijinghao
 * @version : HungrySingleton.java, v 0.1 2019年07月16日 11:45:45 lijinghao Exp $
 */
public class HungrySingleton {

    // finale 是为了防止反射破坏单例
    private static final HungrySingleton INSTANCE = new HungrySingleton();

    /**
     * 私有化构造方法
     */
    private HungrySingleton() {
    }

    /**
     * 提供统一的对外访问路径
     *
      * @return
     */
    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
