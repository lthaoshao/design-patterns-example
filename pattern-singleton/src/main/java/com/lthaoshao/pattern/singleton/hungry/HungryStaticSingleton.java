package com.lthaoshao.pattern.singleton.hungry;

/**
 * <p> 饿汉式-静态块 </p>
 *
 * @author lijinghao
 * @version : HungryStaticSingleton.java, v 0.1 2019年07月16日 11:58:58 lijinghao Exp $
 */
public class HungryStaticSingleton {

    private HungryStaticSingleton() {
    }

    private static final HungryStaticSingleton INSTANCE;

    static {
        INSTANCE = new HungryStaticSingleton();
    }

    public static HungryStaticSingleton getInstance() {
        return INSTANCE;
    }
}
