# [设计模式](../README.md)

## 代理模式(Proxy Pattern)
[TOC]

### 概念

代理模式，指为其他对象提供一种代理，以控制该对象的访问。代理对象在客户端和目标对象之间起到中介的作用，属于结构型设计模式。

### 目的

一是保护对象，二是增强对象。

### 适用场景

在我们的生活中有一些场景，如黄牛买票、婚介、经纪人、租房中介、快递、事务代理等，这些都是代理模式的具体体现。例如，在访问敏感对象时，应该可以检查客户端是否具有所需的访问权限。

### UML

![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-proxy/src/main/resources/proxy.png?raw=true "代理模式")

### 静态代理

代理模式分为了静态代理和动态代理。静态代理就是在编译时就将接口、实现类、代理类全部都手动完成。如果我们需要更多的代理，每一个都需要去手动完成。既浪费时间，也会出现大量的重复代码。

#### 静态代理的实现

首先定义一个顶层接口Travellers

出去旅行，一般都需要买票。

```java
package com.lthaoshao.pattern.proxy;

import java.io.Serializable;

/**
 * <p> 旅行者 </p>
 *
 * @author lijinghao
 * @version : Traveller.java, v 0.1 2019年07月30日 16:43:43 lijinghao Exp $
 */
public interface Travellers extends Serializable {
    /**
     * 买票
     */
    void buyTickets();
}
```

现在有一个学生需要买票，我们定义一个学生类

```java
package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 学生 </p>
 *
 * @author lijinghao
 * @version : Student.java, v 0.1 2019年07月30日 16:42:42 lijinghao Exp $
 */
public class Student implements Travellers {

    @Override
    public void buyTickets() {
        System.out.println("开始买票。。。");
    }
}
```

当然，这个学生可以自己买，也可以让朋友帮忙买。

再定义一个朋友类，现在这个朋友就是这个学生的代理，他替学生去买票。

```java
package com.lthaoshao.pattern.proxy.staticproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 朋友 </p>
 *
 * @author lijinghao
 * @version : Scalpers.java, v 0.1 2019年07月30日 16:47:47 lijinghao Exp $
 */
public class Friend implements Travellers {

    private Student student;
	// 没办法扩展，这个朋友只能帮这个学生买票
    public Friend(Student student) {
        this.student = student;
    }

    @Override
    public void buyTickets() {
        System.out.println("朋友来替学生抢票，获取购票需求");
        student.buyTickets();
        System.out.println("朋友帮学生抢到票了，把票交给学生");
    }
}
```

让我们来测试一下：

```java
package com.lthaoshao.pattern.proxy.staticproxy;

/**
 * <p> 测试类 </p>
 *
 * @author lijinghao
 * @version : StaticProxyTest.java, v 0.1 2019年07月30日 16:50:50 lijinghao Exp $
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        Student student = new Student();
        Friend friend = new Friend(student);
        friend.buyTickets();
    }
}
```

执行结果：

```verilog
朋友来替学生抢票，获取购票需求
开始买票。。。
朋友帮学生抢到票了，把票交给学生
```

从结果中可知，学生的代理-朋友，他们实现了相同的接口，有相同的操作方法，只是代理先是听取的购票需求，然后开始买票，买完后再将票给学生。

其实就是在买票这个操作前，增加了一些操作，在买票后增加了一些操作；

### DBRoute初版

在分布式业务场景中，我们通常会对数据库进行分库分表，分库分表之后使用 Java 操作时，就可能需要配置多个数据源，我们通过设置数据源路由来动态切换数据源。

先定义一个Order订单实体：

```java
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
```

创建DAO持久层：

```java
package com.lthaoshao.pattern.proxy.dbroute;
/**
 * <p> 数据持久层 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:35
 */
public class OrderDao {

   int insert(Order order){
       System.out.println("完成订单插入操作");
       return 1;
   }
}
```

现在，顶一个订单服务的接口：

```JAVA
package com.lthaoshao.pattern.proxy.dbroute;

/**
 * <p> 订单服务 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:33
 */
public interface IOrderService {
    /**
     * 创建订单
     *
     * @return
     */
    String createOrder(Order order);
}
```

创建一个类来实现订单服务：

```java
package com.lthaoshao.pattern.proxy.dbroute;

/**
 * <p> 订单服务实现类 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:34
 */
public class OrderServiceImpl implements IOrderService {
    private OrderDao orderDao;
    public OrderServiceImpl() {
        // 如采用Spring，应是自动注入的
        // 这里为了方便直接创建
        this.orderDao = new OrderDao();
    }

    @Override
    public String createOrder(Order order) {
        System.out.println("OrderService调用createOrder完成创建订单");
        orderDao.insert(order);
        return "完成创建订单";
    }
}
```

接下来我们使用动态代理来进行处理，完成的主要功能就是根据年份不同自动进行分库。根据开闭原则，我们不去修改原来的逻辑，而是通过代理模式来完成。

先创建数据源路由对象，这里使用ThreadLocal来实现单例：

```java
package com.lthaoshao.pattern.proxy.dbroute;

/**
 * <p> 动态数据源 </p>
 *
 * @author lijinghao
 * @version : DynamicDataSourceEntry.java, v 0.1 2019年07月30日 17:53:53 lijinghao Exp $
 */
public class DynamicDataSourceEntry {

    /**
     * 默认数据源
     */
    private static final String DEFAULT_DATASOURCE = null;

    /**
     * 利用ThreadLocal形式的单例
     */
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    /**
     * 私有化构造
     */
    private DynamicDataSourceEntry() {
    }

    /**
     * 设置数据源
     *
     * @param name
     */
    public static void set(String name) {
        LOCAL.set(name);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String get() {
        return LOCAL.get();
    }

    /**
     * 清空
     *
     * @return
     */
    public static boolean clear() {
        LOCAL.remove();
        return true;
    }

    /**
     * 重置
     */
    public static void restore() {
        LOCAL.set(DEFAULT_DATASOURCE);
    }

    /**
     * 根据年份设置数据源
     *
     * @param year
     */
    public static void setByYear(int year){
        LOCAL.set("DS_"+year);
    }
}
```

创建对应的代理类：

```java
package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;
import com.lthaoshao.pattern.proxy.dbroute.IOrderService;
import com.lthaoshao.pattern.proxy.dbroute.Order;

import java.text.SimpleDateFormat;

/**
 * <p> 订单服务静态代理 </p>
 *
 * @author lijinghao
 * @version : OrderServiceStaticProxy.java, v 0.1 2019年07月30日 18:06:06 lijinghao Exp $
 */
public class OrderServiceStaticProxy implements IOrderService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    private IOrderService orderService;

    public OrderServiceStaticProxy(IOrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public String createOrder(Order order) {
        doBefore();
        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_"+ sdf.format(order.getCreateTime()));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());
        String result = orderService.createOrder(order);
        doAfter();
        // 使用完成后需要进行重置数据源
        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
        return result;
    }

    private void doAfter() {
        System.out.println("Proxy after method");
    }

    private void doBefore() {
        System.out.println("Proxy before method");
    }
}
```

​	下面来测试一下：

```java
package com.lthaoshao.pattern.proxy.dbroute;

import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByCglib;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/30 0:04
 */
public class DbRouteProxyTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static void main(String[] args) {
        Order order = new Order();
        order.setId(System.currentTimeMillis());
        order.setDesc("Iphone");
        order.setAmount(8000000); 

        // order.setDate(new Date());
        try {
            order.setCreateTime(sdf.parse("2017-02-11"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        IOrderService orderService = new OrderServiceImpl();
        IOrderService service = new OrderServiceStaticProxy(orderService);
        String result = service.createOrder(order);
        System.out.println(result);
    }
}
```

执行结果：

```verilog
Proxy before method
自动分配数据源到:DB_2017
OrderService调用createOrder完成创建订单
完成订单插入操作
Proxy after method
重置数据源到:null
完成创建订单

Process finished with exit code 0
```

这是符合我们的预期的。来看下通过IDEA生成的类图，是不是跟前面画的一致



![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-proxy/src/main/resources/OrderServiceStaticProxy.png?raw=true "静态代理模式")



### 动态代理

#### JDK动态代理

#### cglib动态代理

#### 自定义动态代理

书写代理类, 编译成字节码文件, 并加载到内存中;

### DBRoute改版

### 总结

保存代理类字节码文件, 并进行反编译查看内容;