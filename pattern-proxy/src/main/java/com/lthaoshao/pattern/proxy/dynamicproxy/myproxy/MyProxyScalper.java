package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.lang.reflect.Method;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 18:57
 */
public class MyProxyScalper implements MyInvocationHandler {
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        Object instance = MyProxy.newProxyInstance(new MyClassLoader(), clazz.getInterfaces(), this);
        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        before();
        // 这里需要注意, 非常容易写成这个样子, 会导致循环调用
        // Object result = method.invoke(proxy, args);
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("仿JDK动态代理 - 前置处理");
    }

    private void after() {
        System.out.println("仿JDK动态代理 - 后置处理");
    }
}
