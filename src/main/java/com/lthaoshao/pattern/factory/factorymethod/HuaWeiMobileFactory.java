package com.lthaoshao.pattern.factory.factorymethod;

import com.lthaoshao.pattern.factory.HuaWei;
import com.lthaoshao.pattern.factory.IMobile;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 20:45
 */
public class HuaWeiMobileFactory implements IMobileFactory {

    public IMobile produce() {
        return new HuaWei();
    }
}
