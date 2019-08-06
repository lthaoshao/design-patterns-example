package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;
import com.lthaoshao.pattern.proxy.dbroute.IOrderService;
import com.lthaoshao.pattern.proxy.dbroute.Order;

import java.text.SimpleDateFormat;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : OrderServiceStaticProxy.java, v 0.1 2019年07月30日 18:06:06 lijinghao Exp $
 */
public class OrderServiceStaticProxy implements IOrderService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    private IOrderService orderService;

    public OrderServiceStaticProxy(IOrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public String createOrder(Order order) {
        doBefore();

        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_"+ sdf.format(order.getCreateTime()));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());

        String result = orderService.createOrder(order);

        doAfter();

        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
        return result;
    }

    private void doAfter() {
        System.out.println("Proxy after method");
    }

    private void doBefore() {
        System.out.println("Proxy before method");
    }
}
