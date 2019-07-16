package com.lthaoshao.pattern.singleton.destroy;

import com.lthaoshao.pattern.singleton.lazy.DoubleCheckLazySingleton;
import com.lthaoshao.pattern.singleton.lazy.InnerClassLazySingleton;

import java.lang.reflect.Constructor;

/**
 * <p> 虽然不会有人恶意的去破坏单例,但是为了避免被误用,还是需要谨慎 </p>
 *
 * @author lijinghao
 * @version : DestroySingletonByReflect.java, v 0.1 2019年07月16日 19:19:19 lijinghao Exp $
 */
public class DestroySingletonByReflect {

    public static void main(String[] args) {
        // 通过反射来破坏单例
        try {
            // 二次检查式单例
            dubboCheck();

            System.out.println("----------------");

            // 内部类式
            InnerClassLazySingleton singleton = InnerClassLazySingleton.getInstance();
            System.out.println(singleton);
            Class<InnerClassLazySingleton> clazz = InnerClassLazySingleton.class;
            Object instance = getObject(clazz);
            System.out.println(instance);
            System.out.println(singleton == instance);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void dubboCheck() throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        DoubleCheckLazySingleton singleton = DoubleCheckLazySingleton.getInstance();
        System.out.println(singleton);

        // 获取Class对象
        Class<?> clazz = DoubleCheckLazySingleton.class;
        Object instance = getObject(clazz);
        System.out.println(instance);

        // 结果为false
        System.out.println(singleton == instance);
    }

    private static Object getObject(Class<?> clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        // 通过反射获取构造, 一定要使用这个getDeclaredConstructor, 声明的构造
        Constructor<?> c = clazz.getDeclaredConstructor(null);
        // 设置暴力访问,私有的方法要设置true后才可以访问,否则会有IllegalAccessException异常
        c.setAccessible(true);

        // 强制获取实例
        return c.newInstance();
    }
}
