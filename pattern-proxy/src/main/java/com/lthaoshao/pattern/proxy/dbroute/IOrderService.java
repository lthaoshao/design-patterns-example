package com.lthaoshao.pattern.proxy.dbroute;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:33
 */
public interface IOrderService {
    /**
     * 创建订单
     *
     * @return
     */
    String createOrder(Order order);
}
