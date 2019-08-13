package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p> test </p>
 *
 * @author lijinghao
 * @version : JDKProxyTest.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        Travellers traveller = (Travellers)new JDKScalper().getInstance(new Customer());

        // 获取到这个class对象的字节流
        byte[] proxy = ProxyGenerator.generateProxyClass("Travellers$Proxy0", new Class[]{Travellers.class});
        // 把这个代理对象输出到文件，之后再进行jad反编译
        try (FileOutputStream os = new FileOutputStream("pattern-proxy/src/main/resources/Travellers$Proxy0.class")) {
            os.write(proxy);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        traveller.buyTickets();
    }
}
