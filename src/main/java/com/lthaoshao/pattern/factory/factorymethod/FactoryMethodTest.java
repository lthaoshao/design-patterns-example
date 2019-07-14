package com.lthaoshao.pattern.factory.factorymethod;

import com.lthaoshao.pattern.factory.IMobile;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 20:44
 */
public class FactoryMethodTest {

    public static void main(String[] args) {
        XiaoMiMobileFactory xiaoMiMobileFactory = new XiaoMiMobileFactory();
        IMobile xiaomi = xiaoMiMobileFactory.produce();
        xiaomi.call();

        HuaWeiMobileFactory huaWeiMobileFactory = new HuaWeiMobileFactory();
        IMobile huawei = huaWeiMobileFactory.produce();
        huawei.call();
    }

}
