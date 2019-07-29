package com.lthaoshao.pattern.singleton.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 容器式单例 </p>
 * 也属于注册式单例的一种.
 * Spring 的IOC 就采用这样的一种形式.
 *
 * @author lijinghao
 * @version : ContainerSingleton.java, v 0.1 2019年07月18日 14:11:11 lijinghao Exp $
 */
public class ContainerSingleton {

    private ContainerSingleton() {
    }

    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    public static Object getInstance(String className) {

            synchronized (map) {
                if (!map.containsKey(className)) {
                    Object obj;
                    try {
                        Class<?> clazz = Class.forName(className);
                        obj = clazz.newInstance();
                        map.put(className, obj);
                        return obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        return map.get(className);
    }
}
