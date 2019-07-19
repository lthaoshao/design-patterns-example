package com.lthaoshao.pattern.singleton.register;

import com.lthaoshao.pattern.singleton.ConcurrentExecutor;
import com.lthaoshao.pattern.singleton.lazy.SimpleLazySingleton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : RegisterSingletonTest.java, v 0.1 2019年07月18日 14:16:16 lijinghao Exp $
 */
public class RegisterSingletonTest {

    public static void main(String[] args) {

        try {
            ConcurrentExecutor.execute(() -> {
                String className = "com.lthaoshao.pattern.singleton.register.ContainerSingleton";
                Object instance = ContainerSingleton.getInstance(className);
                System.out.println(System.currentTimeMillis() + " : " + instance);
            }, 30, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // enumTest();


    }

    private static void enumTest() {

        // 破坏枚举式单例
        String path = "src/main/resources/com.lthaoshao.pattern.singleton/EnumSingleton.obj";
        EnumSingleton instance = EnumSingleton.getInstance();
        instance.setData(new Object());

        // 创建一个实体类

        // 并把它写到文件中
        // 序列化, 就是把内存中的状态, 通过转换成字节码的形式,从而转换成一个输出流, 写入到其他地方(磁盘/网络IO)
        // 内存中的状态就被永久保存下来了
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(instance);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 反序列化, 就是将已经持久化的字节码内容, 转换成IO流的形式, 通过IO流进行读取,
        // 进而将读取的内日转换成对象, 在转换的过程中会对对象重新new
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {

            // 读取实体类
            Object o = ois.readObject();

            // 在加readResolve之前, 这两个地址是不等的, 破坏了单例
            System.out.println(instance);
            System.out.println(o);
            System.out.println(o == instance);

            System.out.println(instance.getData());
            System.out.println(((EnumSingleton) o).getData());
            System.out.println(instance.getData() == ((EnumSingleton) o).getData());

            // 结果是ture, 没有被破坏
            // JDK已经做了相关处理: ois.readObject()
            // java.io.ObjectInputStream
            // 2011   Enum<?> en = Enum.valueOf((Class)cl, name);
            // 根据枚举的名称和类型来获取枚举,必然只有一个.
            // public static EnumSingleton valueOf(String name)
            // {
            //   return (EnumSingleton)Enum.valueOf(com/lthaoshao/pattern/singleton/register/EnumSingleton, name);
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
