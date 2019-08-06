package com.lthaoshao.pattern.proxy;

import java.io.Serializable;

/**
 * <p> 旅行者 </p>
 *
 * @author lijinghao
 * @version : Traveller.java, v 0.1 2019年07月30日 16:43:43 lijinghao Exp $
 */
public interface Traveller extends Serializable {

    /**
     * 买火车票
     */
    void buyTrainTickets();
}
