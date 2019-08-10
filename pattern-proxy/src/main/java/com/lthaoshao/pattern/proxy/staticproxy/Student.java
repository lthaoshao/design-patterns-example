package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 学生 </p>
 *
 * @author lijinghao
 * @version : Student.java, v 0.1 2019年07月30日 16:42:42 lijinghao Exp $
 */
public class Student implements Travellers {

    @Override
    public void buyTickets() {
        System.out.println("开始买票。。。。。");
    }
}
