package com.lthaoshao.pattern.singleton.destroy;

import com.lthaoshao.pattern.singleton.lazy.DoubleCheckLazySingleton;
import com.lthaoshao.pattern.singleton.lazy.InnerClassLazySingleton;
import com.lthaoshao.pattern.singleton.register.EnumSingleton;

import java.lang.reflect.Constructor;

/**
 * <p> 通过反射破坏单例 </p>
 * <p>
 * 可以通过在私有的构造方法中增加判断来拒绝通过反射创建实例
 *
 * @author lijinghao
 * @version : DestroySingletonByReflect.java, v 0.1 2019年07月16日 19:19:19 lijinghao Exp $
 */
public class DestroySingletonByReflect {

    public static void main(String[] args) {
        try {
            // 二次检查式单例
            // dubboCheck();

            // 内部类式
            // innerClass();

            // 枚举单例
            enumSingleton();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void enumSingleton() throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        EnumSingleton instance = EnumSingleton.getInstance();
        instance.setData(new Object());

        Class<EnumSingleton> clazz = EnumSingleton.class;
        // 通过JAD反编译后查看, 发现不存在无参构造
        // EnumSingleton object = (EnumSingleton)getObject(clazz);

        // 采用有参构造来创建
        Constructor<EnumSingleton> c = clazz.getDeclaredConstructor(String.class, int.class);
        c.setAccessible(true);
        EnumSingleton object = c.newInstance("Test", 123);
        // 报错如下
        // java.lang.IllegalArgumentException: Cannot reflectively create enum objects
        // 	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)

        // JDK在底层已经处理好了枚举的安全问题: c.newInstance进去, 源码如下
        // if ((clazz.getModifiers() & Modifier.ENUM) != 0)
        //    throw new IllegalArgumentException("Cannot reflectively create enum objects");

        System.out.println(instance == object);
        System.out.println(instance.getData() == object.getData());
    }


    private static void innerClass() throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        InnerClassLazySingleton singleton = InnerClassLazySingleton.getInstance();
        System.out.println(singleton);
        Class<InnerClassLazySingleton> clazz = InnerClassLazySingleton.class;
        Object instance = getObject(clazz);
        System.out.println(instance);
        System.out.println(singleton == instance);
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
        Constructor<?> c = clazz.getDeclaredConstructor();
        // 设置暴力访问,私有的方法要设置true后才可以访问,否则会有IllegalAccessException异常
        c.setAccessible(true);

        // 强制获取实例
        return c.newInstance();
    }
}
