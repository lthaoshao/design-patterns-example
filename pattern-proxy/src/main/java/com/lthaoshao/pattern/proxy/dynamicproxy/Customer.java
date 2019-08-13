package com.lthaoshao.pattern.proxy.dynamicproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 客户类 </p>
 *
 * @author lijinghao
 * @version : Customer.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class Customer implements Travellers {

    @Override
    public void buyTickets() {
        System.out.println("开始买票。。。");
    }
}
