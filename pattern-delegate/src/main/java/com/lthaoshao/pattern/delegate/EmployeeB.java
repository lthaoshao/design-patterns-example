package com.lthaoshao.pattern.delegate;
/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 14:00
 */
public class EmployeeB implements IEmployee {

    @Override
    public void doing(String command) {
        System.out.println("员工B开始做" + command + "工作了。。。");
    }
}
