package com.lthaoshao.pattern.singleton.lazy;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : LazySingletonTest.java, v 0.1 2019年07月16日 15:28:28 lijinghao Exp $
 */
public class LazySingletonTest {

    public static void main(String[] args) {

        // SimpleLazySingleton lazySingleton1 = SimpleLazySingleton.getInstance();
        // SimpleLazySingleton lazySingleton2 = SimpleLazySingleton.getInstance();
        // System.out.println(lazySingleton1 == lazySingleton2);


        Thread thread = new Thread(new Task());
        Thread thread1 = new Thread(new Task());
        thread.start();
        thread1.start();

        System.out.println("-----------------------");


    }
}
