package com.lthaoshao.pattern.factory.abstractfactory;

/**
 * <p> 抽象工厂 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 22:35
 */
public interface DeviceFactory {

    /**
     * 生产Pad
     *
     * @return
     */
    Pad producePad();

    /**
     * 生产Watch
     *
     * @return
     */
    Watch produceWatch();
}
