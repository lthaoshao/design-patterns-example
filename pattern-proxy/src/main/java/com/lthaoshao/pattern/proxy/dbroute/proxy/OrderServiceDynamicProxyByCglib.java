package com.lthaoshao.pattern.proxy.dbroute.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/10 9:30
 */
public class OrderServiceDynamicProxyByCglib implements MethodInterceptor {

    public Object getInstance(Object o) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(o.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        Object o = proxy.invokeSuper(obj, args);
        after();
        return o;
    }

    private void after() {
        System.out.println("CGLIB 动态代理 执行后操作");
    }

    private void before() {
        System.out.println("CGLIB 动态代理 执行前操作");
    }
}
