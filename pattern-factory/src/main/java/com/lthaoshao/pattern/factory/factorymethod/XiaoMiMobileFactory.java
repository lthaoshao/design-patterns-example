package com.lthaoshao.pattern.factory.factorymethod;

import com.lthaoshao.pattern.factory.IMobile;
import com.lthaoshao.pattern.factory.XiaoMi;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 20:44
 */
public class XiaoMiMobileFactory implements IMobileFactory {

    public IMobile produce() {
        return new XiaoMi();
    }
}
