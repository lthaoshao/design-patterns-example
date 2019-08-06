package com.lthaoshao.pattern.proxy.staticproxy;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : StaticProxyTest.java, v 0.1 2019年07月30日 16:50:50 lijinghao Exp $
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        Student student = new Student();
        Friend scalpers = new Friend(student);
        scalpers.buyTrainTickets();
    }
}
