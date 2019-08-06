package com.lthaoshao.pattern.proxy.dbroute;

import lombok.Data;

import java.util.Date;

/**
 * <p> 订单 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:37
 */
@Data
public class Order {

    private long id;
    private String desc;
    private int amount;
    private Date createTime;
}
