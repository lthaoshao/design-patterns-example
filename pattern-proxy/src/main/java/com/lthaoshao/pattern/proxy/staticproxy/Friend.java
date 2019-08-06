package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Traveller;

/**
 * <p> 黄牛党 </p>
 *
 * @author lijinghao
 * @version : Scalpers.java, v 0.1 2019年07月30日 16:47:47 lijinghao Exp $
 */
public class Friend implements Traveller {

    private Student student;

    public Friend(Student student) {
        this.student = student;
    }

    @Override
    public void buyTrainTickets() {
        System.out.println("让朋友来抢购火车票");
        student.buyTrainTickets();
        System.out.println("已买到火车票");
    }
}
