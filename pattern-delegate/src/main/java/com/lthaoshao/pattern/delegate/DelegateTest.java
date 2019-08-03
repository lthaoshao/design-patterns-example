package com.lthaoshao.pattern.delegate;
/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 14:11
 */
public class DelegateTest {

    public static void main(String[] args) {
        Boss boss = new Boss();
        boss.command("网关", new Leader());
    }
}
