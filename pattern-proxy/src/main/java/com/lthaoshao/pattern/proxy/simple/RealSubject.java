package com.lthaoshao.pattern.proxy.simple;
/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:10
 */
public class RealSubject implements Subject {

    @Override
    public void operation() {
        System.out.println("RealSubject operation");
    }
}
