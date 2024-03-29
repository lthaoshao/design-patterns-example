package com.lthaoshao.pattern.proxy.dbroute;

import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByCglib;
import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByJDK;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p> test </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/30 0:04
 */
public class DbRouteProxyTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static void main(String[] args) {
        Order order = new Order();
        order.setId(System.currentTimeMillis());
        order.setDesc("Iphone");
        order.setAmount(8000000);

        // order.setDate(new Date());
        try {
            order.setCreateTime(sdf.parse("2017-02-11"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        IOrderService orderService = new OrderServiceImpl();
        // IOrderService service = new OrderServiceStaticProxy(orderService);
        // OrderServiceDynamicProxyByJDK orderProxy = new OrderServiceDynamicProxyByJDK();
        OrderServiceDynamicProxyByCglib orderProxy = new OrderServiceDynamicProxyByCglib();
        IOrderService service = (IOrderService)orderProxy.getInstance(orderService);
        String result = service.createOrder(order);
        System.out.println(result);
    }
}
