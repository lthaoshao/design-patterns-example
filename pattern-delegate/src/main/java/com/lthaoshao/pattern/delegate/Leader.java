package com.lthaoshao.pattern.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 14:03
 */
public class Leader implements IEmployee {

    // 项目经理需要提前了解自己的下属都会做什么，擅长什么
    private Map<String, IEmployee> subordinates = new HashMap<>();

    public Leader() {
        subordinates.put("网关", new EmployeeA());
        subordinates.put("支付", new EmployeeB());
    }

    @Override
    public void doing(String command) {
        // 项目经理做事情，发给下面人来做
        subordinates.get(command).doing(command);
    }
}
