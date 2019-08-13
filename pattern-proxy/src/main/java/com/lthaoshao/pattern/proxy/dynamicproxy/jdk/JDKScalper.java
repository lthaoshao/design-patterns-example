package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import java.io.Serializable;
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

    /**
     * 获取代理实例
     *
     * @param target
     * @return
     */
    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置操作
        doBefore();
        // 执行被代理方法
        Object result = method.invoke(this.target, args);
        // 后置操作
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
