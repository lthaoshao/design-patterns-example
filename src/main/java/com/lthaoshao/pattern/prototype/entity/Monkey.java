package com.lthaoshao.pattern.prototype.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 猴子 </p>
 *
 * @author lijinghao
 * @version : Monkey.java, v 0.1 2019年07月26日 17:11:11 lijinghao Exp $
 */
public class Monkey implements Serializable {

    protected int height;
    protected int weight;
    protected Date birthday;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
