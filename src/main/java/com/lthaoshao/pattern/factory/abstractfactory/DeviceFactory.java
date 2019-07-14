package com.lthaoshao.pattern.factory.abstractfactory;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 22:35
 */
public interface DeviceFactory {

    Pad producePad();

    Watch produceWatch();
}
