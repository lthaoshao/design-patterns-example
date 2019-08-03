package com.lthaoshao.pattern.delegate.mvc;
/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 17:25
 */
public class OrderController {

    public void getOrderByMid(String mid){
        System.out.println("通过[mid="+mid+"]获取订单信息");
    }
}
