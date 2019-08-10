package com.lthaoshao.pattern.proxy.dbroute;

/**
 * <p> 订单服务实现类 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:34
 */
public class OrderServiceImpl implements IOrderService {
    private OrderDao orderDao;
    public OrderServiceImpl() {
        // 如采用Spring，应是自动注入的
        // 这里为了方便直接创建
        this.orderDao = new OrderDao();
    }

    @Override
    public String createOrder(Order order) {
        System.out.println("OrderService调用createOrder完成创建订单");
        orderDao.insert(order);
        return "完成创建订单";
    }
}
