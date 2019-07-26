package com.lthaoshao.pattern.prototype;

/**
 * <p>  </p>
 *
 * @author lijinghao
 * @version : PrototypeClient.java, v 0.1 2019年07月26日 16:54:54 lijinghao Exp $
 */
public class PrototypeClient {

    private Prototype prototype;

    public PrototypeClient(Prototype prototype) {
        this.prototype = prototype;
    }

    public Prototype startClone(){
        return prototype.clone();
    }

}
