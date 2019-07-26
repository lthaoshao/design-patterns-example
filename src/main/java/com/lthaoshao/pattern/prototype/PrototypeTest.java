package com.lthaoshao.pattern.prototype;

import com.lthaoshao.pattern.prototype.entity.ConCretePrototype;
import com.lthaoshao.pattern.prototype.entity.Hobby;
import com.lthaoshao.pattern.prototype.entity.QiTianDaSheng;

import java.util.ArrayList;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : PrototypeTest.java, v 0.1 2019年07月26日 16:57:57 lijinghao Exp $
 */
public class PrototypeTest {

    public static void main(String[] args) {
        // test0();

        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();

        try {
            // 深克隆
            QiTianDaSheng clone = (QiTianDaSheng) qiTianDaSheng.clone();

            System.out.println("深克隆:" +(clone.getJinGuBang() == qiTianDaSheng.getJinGuBang()));

            // 浅克隆
            QiTianDaSheng shallowClone = qiTianDaSheng.shallowClone(qiTianDaSheng);
            System.out.println("浅克隆: "+ (shallowClone.getJinGuBang() == qiTianDaSheng.getJinGuBang()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 单例模式防止被破坏
        // 不实现Cloneable 接口 或实现后直接响应实例 或实现 readResolve

        // ArrayList list = new ArrayList();
        // 实现了Cloneable接口, clone()中调用了Arrays.copyOf()方法, 最终调用底层的native方法, 重新分配了内存.


    }

    private static void test0() {
        ConCretePrototype conCretePrototype = new ConCretePrototype();
        conCretePrototype.setAge(18);
        conCretePrototype.setName("上官婉儿");
        conCretePrototype.setHobbies(new Hobby());
        System.out.println(conCretePrototype);

        PrototypeClient client = new PrototypeClient(conCretePrototype);
        // 这种克隆是一种浅克隆, 是一种引用的克隆
        // 当修改了克隆对象的引用对象的值时,原对象的引用对象的值也会跟着变动.
        ConCretePrototype prototype = (ConCretePrototype) client.startClone();

        System.out.println(prototype);

        System.out.println("克隆对象是否相等:" + (conCretePrototype == prototype));
        System.out.println("原始对象中引用类型:" + conCretePrototype.getHobbies());
        System.out.println("克隆对象中引用类型:" + prototype.getHobbies());
        System.out.println("克隆对象的引用对象地址是否相等:" + (conCretePrototype.getHobbies() == prototype.getHobbies()));
    }
}
