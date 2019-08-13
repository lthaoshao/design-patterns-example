package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <p> cglib动态代理 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/8 21:57
 */
public class CglibScalper implements MethodInterceptor {

    public Object getInstance(Class<?> clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        doBefore();

        Object obj = methodProxy.invokeSuper(o, objects);

        doAfter();
        return obj;
    }

    private void doBefore() {
        System.out.println("cglib proxy，before 执行");
    }

    private void doAfter() {
        System.out.println("cglib proxy，after 执行");
    }
}
