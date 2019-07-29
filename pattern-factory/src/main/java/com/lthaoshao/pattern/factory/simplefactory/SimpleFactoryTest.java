package com.lthaoshao.pattern.factory.simplefactory;

import com.lthaoshao.pattern.factory.IMobile;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 19:37
 */
public class SimpleFactoryTest {

    public static void main(String[] args) {

        // 小米
        IMobile xiaomi = MobileFactory.produce("xiaomi");
        xiaomi.call();

        // 华为
        IMobile huawei = MobileFactory.produce("huawei");
        huawei.call();
    }
}
