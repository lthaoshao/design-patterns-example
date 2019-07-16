package com.lthaoshao.pattern.singleton.hungry;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : HungrySingletonTest.java, v 0.1 2019年07月16日 12:00:00 lijinghao Exp $
 */
public class HungrySingletonTest {

    public static void main(String[] args) {

        HungrySingleton instance1 = HungrySingleton.getInstance();
        HungrySingleton instance2 = HungrySingleton.getInstance();
        System.out.println(instance1 == instance2);

        System.out.println("-----------------------");

        HungryStaticSingleton s1 = HungryStaticSingleton.getInstance();
        HungryStaticSingleton s2 = HungryStaticSingleton.getInstance();
        System.out.println(s1 == s2);
    }
}
