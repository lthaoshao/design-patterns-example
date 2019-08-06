package com.lthaoshao.pattern.proxy.dbroute;
/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:35
 */
public class OrderDao {

   int insert(Order order){
       System.out.println("完成订单插入操作");
       return 1;
   }
}
