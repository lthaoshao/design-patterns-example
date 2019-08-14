package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> 基于CGLIB动态代理 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/10 9:30
 */
public class OrderServiceDynamicProxyByCglib implements MethodInterceptor {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    private Object target;
    public Object getInstance(Object o) {
        this.target = o;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(o.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before(args[0]);
        Object o = proxy.invokeSuper(obj, args);
        after();
        return o;
    }

    private void after() {
        System.out.println("CGLIB 动态代理 执行后操作");
        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
    }

    private void before(Object target) {
        System.out.println("CGLIB 动态代理 执行前操作");
        Date time = null;
        try {
            time = (Date) target.getClass().getMethod("getCreateTime").invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_" + sdf.format(time));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());
    }
}
