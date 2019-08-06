package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Traveller;

/**
 * <p> 学生 </p>
 *
 * @author lijinghao
 * @version : Student.java, v 0.1 2019年07月30日 16:42:42 lijinghao Exp $
 */
public class Student implements Traveller {

    @Override
    public void buyTrainTickets() {
        System.out.println("购票需求: XXX时间段  XXX或YYY车次 XXX或YYY席位");
    }
}
