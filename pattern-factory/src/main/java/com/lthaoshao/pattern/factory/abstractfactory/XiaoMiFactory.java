package com.lthaoshao.pattern.factory.abstractfactory;

/**
 * <p> 小米工厂 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 22:37
 */
public class XiaoMiFactory implements DeviceFactory{

    public Pad producePad() {
        return new XiaoMiPad();
    }

    public Watch produceWatch() {
        return new XiaoMiWatch();
    }
}
