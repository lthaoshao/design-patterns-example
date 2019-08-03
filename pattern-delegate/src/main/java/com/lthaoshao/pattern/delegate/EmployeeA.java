package com.lthaoshao.pattern.delegate;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 14:00
 */
public class EmployeeA implements IEmployee {

    @Override
    public void doing(String command) {
        System.out.println("员工A开始" + command + "工作了。。。");
    }
}
