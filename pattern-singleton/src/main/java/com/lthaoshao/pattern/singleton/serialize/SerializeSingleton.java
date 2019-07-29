package com.lthaoshao.pattern.singleton.serialize;

import java.io.Serializable;

/**
 * <p> 实现了序列化的单例 </p>
 *
 * @author lijinghao
 * @version : SerializeSingleton.java, v 0.1 2019年07月17日 17:38:38 lijinghao Exp $
 */
public class SerializeSingleton implements Serializable {


    private SerializeSingleton() {
    }

    private static final SerializeSingleton INSTANCE = new SerializeSingleton();


    public static SerializeSingleton getInstance() {
        return INSTANCE;
    }

    /**
     * 添加readResolve, 来防止因对象序列化而破坏单例
     * ObjectInputStream 读取对象后会先创建一个实例, 如果实例中存在readResolve()方法,
     * 则会调用该方法,如返回的实例与之前序列化创建的实例不是一个,则会用新的实例替换掉.
     *
     * @return
     */
    public Object readResolve() {
        return INSTANCE;
    }
}
