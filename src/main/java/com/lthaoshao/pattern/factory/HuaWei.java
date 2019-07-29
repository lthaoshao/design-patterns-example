package com.lthaoshao.pattern.factory;

import com.lthaoshao.pattern.factory.IMobile;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 19:33
 */
public class HuaWei implements IMobile {
    @Override
    public void call() {
        System.out.println("正在使用华为手机打电话...");
    }
}
