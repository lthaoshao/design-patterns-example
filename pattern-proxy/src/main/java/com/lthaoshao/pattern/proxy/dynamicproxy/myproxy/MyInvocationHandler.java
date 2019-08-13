package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.lang.reflect.Method;

/**
 * <p> 自定义调用处理程序 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 10:21
 */
public interface MyInvocationHandler {

    /**
     * 执行方法
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
