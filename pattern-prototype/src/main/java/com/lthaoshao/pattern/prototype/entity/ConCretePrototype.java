package com.lthaoshao.pattern.prototype.entity;

import com.lthaoshao.pattern.prototype.Prototype;

/**
 * <p> 一个具体的需要克隆的对象 </p>
 *
 * @author lijinghao
 * @version : ConCretePrototype.java, v 0.1 2019年07月26日 16:51:51 lijinghao Exp $
 */
public class ConCretePrototype implements Prototype {

    private int age;
    private String name;
    private Hobby hobbies;

    /**
     * 浅克隆
     *
     * @return
     */
    @Override
    public Prototype clone() {

        ConCretePrototype conCretePrototype = new ConCretePrototype();
        conCretePrototype.setAge(this.age);
        conCretePrototype.setName(this.name);
        conCretePrototype.setHobbies(this.hobbies);
        return conCretePrototype;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hobby getHobbies() {
        return hobbies;
    }

    public void setHobbies(Hobby hobbies) {
        this.hobbies = hobbies;
    }
}
