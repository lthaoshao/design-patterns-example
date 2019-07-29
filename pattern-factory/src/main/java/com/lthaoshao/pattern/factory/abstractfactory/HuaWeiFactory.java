package com.lthaoshao.pattern.factory.abstractfactory;
/**
 * <p> 华为工厂 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 22:47
 */
public class HuaWeiFactory implements DeviceFactory {

    public Pad producePad() {
        return new HuaWeiPad();
    }

    public Watch produceWatch() {
        return new HuaWeiWatch();
    }
}
