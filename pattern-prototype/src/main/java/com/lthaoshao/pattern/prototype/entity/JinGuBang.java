package com.lthaoshao.pattern.prototype.entity;

import java.io.Serializable;

/**
 * <p> 金箍棒 </p>
 *
 * @author lijinghao
 * @version : JinGuBang.java, v 0.1 2019年07月26日 17:13:13 lijinghao Exp $
 */
public class JinGuBang implements Serializable {

    private int h;
    private int d;

    public void big() {
        h *= 2;
        d *= 2;
    }

    public void small() {
        h = h / 2;
        d = d / 2;
    }

}
