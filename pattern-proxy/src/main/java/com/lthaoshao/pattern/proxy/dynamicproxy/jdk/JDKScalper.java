package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import com.lthaoshao.pattern.proxy.Travellers;
import sun.misc.ProxyGenerator;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> JDK动态代理 </p>
 * 黄牛党
 *
 * @author lijinghao
 * @version : JDKMatchmaker.java, v 0.1 2019年07月31日 20:15:15 lijinghao Exp $
 */
public class JDKScalper implements InvocationHandler, Serializable {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);

        // 获取到这个class对象的字节流
        byte[] proxy = ProxyGenerator.generateProxyClass("Traveller$Proxy0", new Class[]{Travellers.class});
        // 把这个代理对象输出到文件，之后再进行jad反编译
        try (FileOutputStream os = new FileOutputStream("pattern-proxy/src/main/resources/Traveller$Proxy0.class")) {
            os.write(proxy);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doBefore();
        Object result = method.invoke(this.target, args);
        doAfter();
        return result;
    }

    private void doAfter() {
        System.out.println("do after：买票完成");
    }

    private void doBefore() {
        System.out.println("do before：听取顾客需求，开始买票");
    }
}
