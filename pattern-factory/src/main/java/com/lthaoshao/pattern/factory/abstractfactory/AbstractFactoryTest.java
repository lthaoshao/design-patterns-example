package com.lthaoshao.pattern.factory.abstractfactory;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 22:46
 */
public class AbstractFactoryTest {

    public static void main(String[] args) {
        XiaoMiFactory xiaoMiFactory = new XiaoMiFactory();
        Pad xiaoMipad = xiaoMiFactory.producePad();
        Watch xiaoMiWatch = xiaoMiFactory.produceWatch();
        xiaoMipad.vedio();
        xiaoMiWatch.count();


        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        Pad huaWeiPad = huaWeiFactory.producePad();
        Watch huaWeiWatch = huaWeiFactory.produceWatch();
        huaWeiPad.vedio();
        huaWeiWatch.count();
    }
}
