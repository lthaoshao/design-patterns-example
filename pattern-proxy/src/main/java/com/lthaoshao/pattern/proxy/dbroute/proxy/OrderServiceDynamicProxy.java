package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : OrderServiceDynamicProxy.java, v 0.1 2019年08月01日 20:34:34 lijinghao Exp $
 */
public class OrderServiceDynamicProxy implements InvocationHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;

        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        doBefore(args[0]);

        String result = (String) method.invoke(target, args);

        doAfter();

        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
        return result;
    }

    private void doAfter() {
        System.out.println("Proxy after method");

    }

    private void doBefore(Object target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Proxy before method");

        Date time = (Date) target.getClass().getMethod("getCreateTime").invoke(target);
        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_" + sdf.format(time));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());
    }

}
