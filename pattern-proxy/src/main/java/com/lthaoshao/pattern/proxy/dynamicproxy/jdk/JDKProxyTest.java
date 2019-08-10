package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : JDKProxyTest.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        Travellers traveller = (Travellers)new JDKScalper().getInstance(new Customer());
        traveller.buyTickets();
    }
}
