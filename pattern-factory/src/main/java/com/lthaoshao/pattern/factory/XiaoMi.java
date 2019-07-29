package com.lthaoshao.pattern.factory;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 19:32
 */
public class XiaoMi implements IMobile {
    @Override
    public void call() {
        System.out.println("正在使用小米手机打电话...");
    }
}
