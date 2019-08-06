package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import com.lthaoshao.pattern.proxy.Traveller;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : Customer.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class Customer implements Traveller {

    @Override
    public void buyTrainTickets() {
        System.out.println("顾客买火车票");
    }
}
