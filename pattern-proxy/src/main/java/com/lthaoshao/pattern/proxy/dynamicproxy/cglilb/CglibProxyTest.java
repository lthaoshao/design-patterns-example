package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;
import net.sf.cglib.core.DebuggingClassWriter;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/8 22:06
 */
public class CglibProxyTest {

    public static void main(String[] args) {

        // 利用cglib的代理类，将内存中的class文件写入磁盘
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "pattern-proxy/src/main/resources/");


        Customer customer = new Customer();
        CglibScalper scalper = new CglibScalper();
        Customer proxy = (Customer) scalper.getInstance(customer.getClass());
        proxy.buyTrainTickets();

    }
}
