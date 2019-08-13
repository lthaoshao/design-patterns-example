package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;

/**
 * <p> test </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 18:59
 */
public class MyProxyTest {

    public static void main(String[] args) {
        Travellers travellers = (Travellers)new MyProxyScalper().getInstance(new Customer());
        travellers.buyTickets();
    }
}
