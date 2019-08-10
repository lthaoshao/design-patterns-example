package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 朋友 </p>
 *
 * @author lijinghao
 * @version : Scalpers.java, v 0.1 2019年07月30日 16:47:47 lijinghao Exp $
 */
public class Friend implements Travellers {

    private Student student;

    public Friend(Student student) {
        this.student = student;
    }

    @Override
    public void buyTickets() {
        System.out.println("朋友来替学生抢票，获取购票需求");
        student.buyTickets();
        System.out.println("朋友帮学生抢到票了，把票交给学生");
    }
}
