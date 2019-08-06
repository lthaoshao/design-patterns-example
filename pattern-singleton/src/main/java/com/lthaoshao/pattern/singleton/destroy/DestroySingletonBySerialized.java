package com.lthaoshao.pattern.singleton.destroy;

import com.lthaoshao.pattern.singleton.serialize.SerializeSingleton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

/**
 * <p> 通过序列化破坏单例 </p>
 *
 * @author lijinghao
 * @version : DestroySingletonBySerialized.java, v 0.1 2019年07月17日 17:36:36 lijinghao Exp $
 */
public class DestroySingletonBySerialized {

    public static void main(String[] args) {

        String path = "pattern-singleton/src/main/resources/SerializeSingleton.obj";
        SerializeSingleton instance = SerializeSingleton.getInstance();

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
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {

            // 读取实体类
            Object o = ois.readObject();

            // 在加readResolve之前, 这两个地址是不等的, 破坏了单例
            System.out.println(instance);
            System.out.println(o);
            System.out.println(o == instance);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
