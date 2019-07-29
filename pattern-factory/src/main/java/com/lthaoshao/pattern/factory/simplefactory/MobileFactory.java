package com.lthaoshao.pattern.factory.simplefactory;

import com.lthaoshao.pattern.factory.HuaWei;
import com.lthaoshao.pattern.factory.IMobile;
import com.lthaoshao.pattern.factory.XiaoMi;

/**
 * <p> 简单工厂 </p>
 * 由工厂决定创建哪一个实例. 不属于23种设计模式
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/14 19:35
 */
public class MobileFactory {

    public static IMobile produce(String name){
        if("xiaomi".equalsIgnoreCase(name)){
            return new XiaoMi();
        }else if("huawei".equalsIgnoreCase(name)){
            return new HuaWei();
        }
        return null;
    }
}
