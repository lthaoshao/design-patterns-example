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

动态代理和静态对比基本思路是一致的，只不过动态代理功能更加强大，随着业务的扩展适应性更强。如果还以找对象为例，使用动态代理相当于是能够适应复杂的业务场景。 这里主要介绍JDK动态代理和CGlib动态dialing

#### JDK动态代理

这里还是使用Travellers接口。创建一个客户类。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy;

import com.lthaoshao.pattern.proxy.Travellers;

/**
 * <p> 客户类 </p>
 *
 * @author lijinghao
 * @version : Customer.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class Customer implements Travellers {

    @Override
    public void buyTickets() {
        System.out.println("开始买票。。。");
    }
}
```

现在我们把买票的工作交给了黄牛党来做。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> JDK动态代理 </p>
 * 黄牛党
 *
 * @author lijinghao
 * @version : JDKMatchmaker.java, v 0.1 2019年07月31日 20:15:15 lijinghao Exp $
 */
public class JDKScalper implements InvocationHandler, Serializable {

    private Object target;

    /**
     * 获取代理实例
     *
     * @param target
     * @return
     */
    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置操作
        doBefore();
        // 执行被代理方法
        Object result = method.invoke(this.target, args);
        // 后置操作
        doAfter();
        return result;
    }

    private void doAfter() {
        System.out.println("do after：买票完成");
    }

    private void doBefore() {
        System.out.println("do before：听取顾客需求，开始买票");
    }
}
```

创建测试代码：

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.jdk;

import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;

/**
 * <p> test </p>
 *
 * @author lijinghao
 * @version : JDKProxyTest.java, v 0.1 2019年07月31日 21:01:01 lijinghao Exp $
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        Travellers traveller = (Travellers)new JDKScalper().getInstance(new Customer());
        traveller.buyTickets();
    }
}
```

执行验证

```verilog
do before：听取顾客需求，开始买票
开始买票。。。
do after：买票完成

Process finished with exit code 0
```

通过上面案例可以看到，现在代理类传入的是Object，即不需要知道它具体的类型了，这样能方便扩展。

下面我们来研究一下JDK是怎么实现动态代理的。

我们知道JDK动态代理是采用字节码重组技术，生成新的对象来代替原始的对象，已达到动态代理的目的。JDK生成代理对象包含以下几个步骤：

<font style="color:red">1、获取被代理对象的引用；</font>

<font style="color:red">2、通过JDK Proxy类重新生成一个新的类，同时新的类也要实现被代理类实现的所有的接口；</font>

<font style="color:red">3、动态生成java代码，把新加的业务逻辑方法由一定的逻辑代码去调用（在代码中体现）；</font>

<font style="color:red">4、编译生成字节码文件；</font>

<font style="color:red">5、重新加载字节码文件到JVM中运行；</font>

上面的这个过程就被称作字节码重组。在JDK 中有一个规范， ClassPath 下只要是$开头的 class 文件一般都是自动生成的。

那JDK动态生成的这个代理对象到底是什么样子的呢？下面我们尝试来看一下。我们从内存中将这个对象的字节码通过文件流输出到本地磁盘上，生成一个新的class文件，然后通过JAD工具进行反编译查看源代码。

修改JDK动态代理测试类

```java
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
        byte[] proxy = ProxyGenerator.generateProxyClass("Traveller$Proxy0", new Class[]{Travellers.class});
        // 把这个代理对象输出到文件，之后再进行jad反编译
        try (FileOutputStream os = new FileOutputStream("pattern-proxy/src/main/resources/Traveller$Proxy0.class")) {
            os.write(proxy);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        traveller.buyTickets();
    }
}

```

然后我们在我们设定的路径下有了一个新的class文件：`Traveller$Proxy0.class`

进入到对应目录下，执行jad反编译命令

```bash
jad Traveller$Proxy0.class
```

得到文件：`Traveller$Proxy0.jad`。当然我们这里也可以直接使用IDEA的JAD反编译插件，直接查看class反编译文件的。

```java
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.lthaoshao.pattern.proxy.Travellers;
import java.lang.reflect.*;

public final class Travellers$Proxy0 extends Proxy
    implements Travellers
{

    public final boolean equals(Object obj)
    {
        try
        {
            return ((Boolean)super.h.invoke(this, m1, new Object[] {
                obj
            })).booleanValue();
        }
        catch(Error _ex) { }
        catch(Throwable throwable)
        {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    public final void buyTickets()
    {
        try
        {
            super.h.invoke(this, m3, null);
            return;
        }
        catch(Error _ex) { }
        catch(Throwable throwable)
        {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    public final String toString()
    {
        try
        {
            return (String)super.h.invoke(this, m2, null);
        }
        catch(Error _ex) { }
        catch(Throwable throwable)
        {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    public final int hashCode()
    {
        try
        {
            return ((Integer)super.h.invoke(this, m0, null)).intValue();
        }
        catch(Error _ex) { }
        catch(Throwable throwable)
        {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;

    static 
    {
        try
        {
            m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] {
                Class.forName("java.lang.Object")
            });
            m3 = Class.forName("com.lthaoshao.pattern.proxy.Travellers").getMethod("buyTickets", new Class[0]);
            m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
            m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            throw new NoSuchMethodError(nosuchmethodexception.getMessage());
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public Travellers$Proxy0(InvocationHandler invocationhandler)
    {
        super(invocationhandler);
    }
}
```

可以看到，新生成的`Traveller$Proxy0`继承了`Proxy`，并且跟被代理类一样实现了我们的`Travellers`接口，重写了`buyTickets`方法。在静态代码块中，通过反射，找到了目标对象的所有方法，而且保存了方法的引用，在重写的方法中调用了目标对象的方法。而这些代码，就是JDK动态帮我们生成的。

#### 仿JDK动态代理

现在我们尝试不使用JDK的动态代理，而是仿照JDK动态代理，由我们自己来实现整个过程。

##### MyInvocationHandler

要使用JDK动态代理，需要实现`InvocationHandler`接口，现在定义自己的MyInvocationHandler接口，同样的设置一下invoke方法。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myrpoxy;

import java.lang.reflect.Method;

/**
 * <p> 我的调用处理程序 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 10:21
 */
public interface MyInvocationHandler {

    /**
     * 执行方法
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```

##### MyProxy

JDK动态代理中使用到了一个非常重要的类，就是Proxy，现在我们来定义MyProxy类。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 自定义代理类 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 10:25
 */
public class MyProxy implements Serializable {

    private static final String LN = "\r\n";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";
    private static final String TAB = "\t";

    // 同样要完成一个获取代理对象的方法 newProxyInstance，要求穿入类加载器，实现的所有接口以及处理器。
    // 但这里我们不能直接使用JDK已定义好的类加载器和程序处理器，需要使用我们自定义好的
    // 在这里newProxyInstance要完成以下步骤：
    // 1. 动态生成代理类的源代码.java文件
    // 2. 将java文件输出到磁盘
    // 3. 编译源代码文件为 .class文件
    // 4. 动态加载.class文件到JVM
    // 5. 返回字节码重组以后的新对象


    /**
     * 生成新的代理对象
     *
     * @param classLoader
     * @param interfaces
     * @param h
     * @return
     */
    public static Object newProxyInstance(MyClassLoader classLoader, Class<?>[] interfaces, MyInvocationHandler h) {

        // 1. 动态生成代理类的源代码.java文件
        String src = generateSrc(interfaces);
        // System.out.println(src);

        // 2. 将java文件输出到磁盘
        String path = MyProxy.class.getResource("").getPath();
        File file = new File(path + "MyTravellers$Proxy0.java");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(src);
            fileWriter.flush();

            // 3. 编译源代码文件为 .class文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(file);

            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, manager, null, null, null, javaFileObjects);
            task.call();
            manager.close();

            // 4. 动态加载.class文件到JVM
            Class<?> clazz = classLoader.findClass("MyTravellers$Proxy0");
            Constructor<?> constructor = clazz.getConstructor(MyInvocationHandler.class);

            file.delete();

            // 返回字节码重组以后的新对象
            return constructor.newInstance(h);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 生成java源代码
     *
     * @param interfaces
     * @return
     */
    private static String generateSrc(Class<?>[] interfaces) {
        // 这里就简单生成具体的代理对象 MyTravellers$Proxy0.java
        // 后面的数字0：当需要生成多个代理对象时，在JDK中会进行++
        // 我们这里只实现一个接口
        // 下面我们来手写一下
        StringBuffer sb = new StringBuffer();

        // 生成package，我们将该文件放到跟MyProxy一个路径下
        sb.append("package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;")
                .append(LN).append(LN);
        sb.append("import java.lang.reflect.*;").append(LN);

        StringBuffer allInterfaces = new StringBuffer();
        // 生成import
        // 要实现的接口
        for (Class<?> anInterface : interfaces) {
            sb.append("import ")
                    .append(anInterface.getName())
                    .append(SEMICOLON)
                    .append(LN);

            allInterfaces.append(anInterface.getSimpleName())
                    .append(COMMA)
                    .append(SPACE);
        }

        // 要继承的MyProxy类
        sb.append("import ")
                .append(MyProxy.class.getName())
                .append(SEMICOLON)
                .append(LN)
                .append(LN);

        // class start
        sb.append("public class ")
                .append("MyTravellers$Proxy0")
                .append(SPACE)
                .append("extends ")
                .append(MyProxy.class.getSimpleName())
                .append(SPACE)
                .append("implements ")
                .append(allInterfaces.toString(), 0, allInterfaces.toString().length() - 2)
                .append(" {")
                .append(LN);


        // 内容
        sb.append(TAB)
                .append("MyInvocationHandler")
                .append(SPACE)
                .append("h")
                .append(SEMICOLON)
                .append(LN).append(LN);
        // 构造方法
        sb.append(TAB)
                .append("public MyTravellers$Proxy0 (MyInvocationHandler h) {")
                .append(LN).append(TAB).append(TAB)
                .append("this.h = h;")
                .append(LN).append(TAB).append("}").append(LN);
        // 覆盖实现的所有接口中的方法
        for (Class<?> anInterface : interfaces) {
            for (Method method : anInterface.getMethods()) {
                // 方法中的参数
                Class<?>[] params = method.getParameterTypes();

                StringBuffer paramNames = new StringBuffer();
                StringBuffer paramValues = new StringBuffer();
                StringBuffer paramClasses = new StringBuffer();

                for (int i = 0; i < params.length; i++) {
                    Class<?> clazz = params[i];
                    String name = clazz.getName();
                    // 设置参数名称
                    // TODO 2019年8月13日 11:47:31
                    String paramName = toLowerFirstCase(clazz.getSimpleName());
                    paramNames.append(name).append(SPACE).append(paramName);
                    paramValues.append(paramName);
                    paramClasses.append(clazz.getName()).append(".class");

                    if (i > 0 && i < params.length - 1) {
                        paramNames.append(COMMA);
                        paramValues.append(COMMA);
                        paramClasses.append(COMMA);
                    }
                }

                // 方法
                sb.append(TAB)
                        .append(LN).append(TAB)
                        .append("@Override")
                        .append(LN)
                        .append(TAB)
                        .append("public ")
                        .append(method.getReturnType().getName()).append(SPACE)
                        .append(method.getName())
                        .append("(").append(paramNames.toString()).append(")")
                        .append(SPACE).append("{").append(LN)
                        // 内容
                        .append(TAB).append(TAB)
                        .append("try {").append(LN)
                        .append(TAB).append(TAB).append(TAB)
                        .append("Method m = ")
                        .append(anInterface.getSimpleName()).append(".class.getMethod(\"")
                        .append(method.getName())
                        .append("\", new Class[]{").append(paramClasses.toString()).append("})")
                        .append(SEMICOLON)
                        .append(LN)
                        // 设置return
                        .append(TAB).append(TAB).append(TAB)
                        .append(hasReturn(method.getReturnType()) ? "return " : "")
                        .append(LN).append(TAB).append(TAB).append(TAB)
                        .append(getCaseCode("this.h.invoke(this,m,new Object[]{" + paramValues + "})", method.getReturnType()) + ";")
                        .append(LN).append(TAB).append(TAB)
                        .append("}catch(Error _ex) {")
                        .append(LN).append(LN)
                        .append(TAB).append(TAB)
                        .append("} catch(Throwable e) {").append(LN)
                        .append(TAB).append(TAB).append(TAB)
                        .append("throw new UndeclaredThrowableException(e);").append(LN)
                        .append(TAB).append(TAB)
                        .append("}")
                        .append(getReturnEmptyCode(method.getReturnType()))
                        .append(LN).append(TAB)
                        .append("}")
                        .append(LN);
            }
        }

        // start end
        sb.append("}");
        return sb.toString();
    }

    private static String getReturnEmptyCode(Class<?> returnClass) {
        if (mappings.containsKey(returnClass)) {
            return "return 0;";
        } else if (returnClass == void.class) {
            return "";
        } else {
            return "return null;";
        }
    }

    private static final Map<Class, Class> mappings = new HashMap<>();

    static {
        mappings.put(int.class, Integer.class);
    }


    private static String getCaseCode(String code, Class<?> returnClass) {
        if (mappings.containsKey(returnClass)) {
            return "((" + mappings.get(returnClass).getName() + ")" + code + ")." +
                    returnClass.getSimpleName() + "Value()";
        }
        return code;
    }

    private static boolean hasReturn(Class<?> returnType) {
        return returnType != void.class;
    }

    private static String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] = String.valueOf(chars[0]).toLowerCase().charAt(0);
        return String.valueOf(chars);
    }

}
```

##### MyClassLoader

自定义类加载器MyClassLoader

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * <p> 自定义类加载器 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 10:31
 */
public class MyClassLoader extends ClassLoader {
    private File classPathFile;

    public MyClassLoader() {
        String path = MyClassLoader.class.getResource("").getPath();
        this.classPathFile = new File(path);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String className = MyClassLoader.class.getPackage().getName() + "." + name;
        if (classPathFile != null) {
            File classFile =
                    new File(classPathFile, name.replace("\\.", "/") + ".class");
            if (classFile.exists()) {
                try (FileInputStream in = new FileInputStream(classFile);
                     ByteArrayOutputStream out = new ByteArrayOutputStream()){

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1){
                        out.write(buffer,0,len);
                    }
                    return defineClass(className,out.toByteArray(),0,out.size());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return super.findClass(name);
    }
}
```

##### MyProxyScalper

好了，现在让我们使用自定义方法来完成动态代理的验证。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.lang.reflect.Method;

/**
 * <p> 黄牛 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 18:57
 */
public class MyProxyScalper implements MyInvocationHandler {
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        Object instance = MyProxy.newProxyInstance(new MyClassLoader(), clazz.getInterfaces(), this);
        return instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        before();
        // 这里需要注意, 非常容易写成这个样子, 会导致循环调用
        // Object result = method.invoke(proxy, args);
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("仿JDK动态代理 - 前置处理");
    }

    private void after() {
        System.out.println("仿JDK动态代理 - 后置处理");
    }
}
```

##### MyProxyTest

测试类

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;

/**
 * <p> test </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/13 18:59
 */
public class MyProxyTest {

    public static void main(String[] args) {
        Travellers travellers = (Travellers)new MyProxyScalper().getInstance(new Customer());
        travellers.buyTickets();
    }
}
```

让我们来执行一下：

```verilog
仿JDK动态代理 - 前置处理
开始买票。。。
仿JDK动态代理 - 后置处理

Process finished with exit code 0
```

处理成功！当然在完成的过程中并不那么顺利。

##### MyTravellers$Proxy0

来看一下我们自定义生成的代理类

打印的结果：

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.lang.reflect.*;
import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.myproxy.MyProxy;

public class MyTravellers$Proxy0 extends MyProxy implements Travellers {
	MyInvocationHandler h;

	public MyTravellers$Proxy0 (MyInvocationHandler h) {
		this.h = h;
	}
	
	@Override
	public void buyTickets() {
		try {
			Method m = Travellers.class.getMethod("buyTickets", new Class[]{});
			
			this.h.invoke(this,m,new Object[]{});
		}catch(Error _ex) {

		} catch(Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}
}
```

写到文件里的结果：

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import java.lang.reflect.*;
import com.lthaoshao.pattern.proxy.Travellers;
import com.lthaoshao.pattern.proxy.dynamicproxy.myproxy.MyProxy;

public class MyTravellers$Proxy0 extends MyProxy implements Travellers {
	MyInvocationHandler h;

	public MyTravellers$Proxy0 (MyInvocationHandler h) {
		this.h = h;
	}
	
	@Override
	public void buyTickets() {
		try {
			Method m = Travellers.class.getMethod("buyTickets", new Class[]{});
			
			this.h.invoke(this,m,new Object[]{});
		}catch(Error _ex) {

		} catch(Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}
}
```

嗯，看上去是一样的。下面看一下class文件反编译的结果(IDEA)。

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lthaoshao.pattern.proxy.dynamicproxy.myproxy;

import com.lthaoshao.pattern.proxy.Travellers;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class MyTravellers$Proxy0 extends MyProxy implements Travellers {
    MyInvocationHandler h;

    public MyTravellers$Proxy0(MyInvocationHandler var1) {
        this.h = var1;
    }

    public void buyTickets() {
        try {
            Method var1 = Travellers.class.getMethod("buyTickets");
            this.h.invoke(this, var1, new Object[0]);
        } catch (Error var2) {
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }

    }
}
```

##### 问题和解决办法

可能出现的报错

```verilog
D:\work\code\open\design-patterns-example\pattern-proxy\target\classes\com\lthaoshao\pattern\proxy\dynamicproxy\myproxy\MyTravellers$Proxy0.java:9: 警告: Can't initialize javac processor due to (most likely) a class loader problem: java.lang.NoClassDefFoundError: com/sun/tools/javac/processing/JavacProcessingEnvironment
public class MyTravellers$Proxy0 extends MyProxy implements Travellers {
       ^
  	at lombok.javac.apt.LombokProcessor.getJavacProcessingEnvironment(LombokProcessor.java:410)
  	at lombok.javac.apt.LombokProcessor.init(LombokProcessor.java:90)
  	at lombok.core.AnnotationProcessor$JavacDescriptor.want(AnnotationProcessor.java:124)
  	at lombok.core.AnnotationProcessor.init(AnnotationProcessor.java:177)
  	at lombok.launch.AnnotationProcessorHider$AnnotationProcessor.init(AnnotationProcessor.java:73)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment$ProcessorState.<init>(JavacProcessingEnvironment.java:500)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment$DiscoveredProcessors$ProcessorStateIterator.next(JavacProcessingEnvironment.java:597)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.discoverAndRunProcs(JavacProcessingEnvironment.java:690)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.access$1800(JavacProcessingEnvironment.java:91)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run(JavacProcessingEnvironment.java:1035)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing(JavacProcessingEnvironment.java:1176)
  	at com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1170)
  	at com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:856)
  	at com.sun.tools.javac.main.Main.compile(Main.java:523)
  	at com.sun.tools.javac.api.JavacTaskImpl.doCall(JavacTaskImpl.java:129)
  	at com.sun.tools.javac.api.JavacTaskImpl.call(JavacTaskImpl.java:138)
  	at com.lthaoshao.pattern.proxy.dynamicproxy.myproxy.MyProxy.newProxyInstance(MyProxy.java:68)
  	at com.lthaoshao.pattern.proxy.dynamicproxy.myproxy.MyProxyScalper.getInstance(MyProxyScalper.java:17)
  	at com.lthaoshao.pattern.proxy.dynamicproxy.myproxy.MyProxyTest.main(MyProxyTest.java:16)
  Caused by: java.lang.ClassNotFoundException: com.sun.tools.javac.processing.JavacProcessingEnvironment
  	at java.lang.ClassLoader.findClass(ClassLoader.java:530)
  	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
  	at lombok.launch.ShadowClassLoader.loadClass(ShadowClassLoader.java:530)
  	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
  	... 19 more
```

解决办法：添加tools.jar到环境中

![1565699090842](C:\Users\huanhuan\AppData\Roaming\Typora\typora-user-images\1565699090842.png)



#### CGLIB动态代理

> cglib(Code Generation Library)是一个开源项目，是一个强大的，高性能，高质量的Code生成类库，它可以在运行期扩展Java类与实现Java接口。
> *--摘自百度百科*

利用上面已有的一些代码，我们这里直接创建基于cflib的代理。这里需要先引入cglib的jar包。

```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.2.6</version>
</dependency>
```

创建代理类：

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <p> cglib动态代理 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/8 21:57
 */
public class CglibScalper implements MethodInterceptor {

    public Object getInstance(Class<?> clazz){
        Enhancer enhancer = new Enhancer();
        // 将被代理类作为要生成的类的父类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        doBefore();
        Object obj = methodProxy.invokeSuper(o, objects);
        doAfter();
        return obj;
    }

    private void doBefore() {
        System.out.println("cglib proxy，before 执行");
    }

    private void doAfter() {
        System.out.println("cglib proxy，after 执行");
    }
}
```

目标对象，这里的目标对象不需要实现任何接口，cglib是通过继承目标接口来实现的。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

public class Customer {
    public void buyTickets() {
        System.out.println("开始买票。。。");
    }
}
```

创建对应的测试类

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import com.lthaoshao.pattern.proxy.dynamicproxy.Customer;
import net.sf.cglib.core.DebuggingClassWriter;

/**
 * <p> test </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/8 22:06
 */
public class CglibProxyTest {

    public static void main(String[] args) {
        Customer customer = new Customer();
        CglibScalper scalper = new CglibScalper();
        Customer proxy = (Customer) scalper.getInstance(customer.getClass());
        proxy.buyTickets();
    }
}
```

执行结果：

```verilog
cglib proxy，before 执行
开始买票。。。
cglib proxy，after 执行

Process finished with exit code 0
```

结果符合我们的预期。那么CGLIB是怎么实现动态代理的呢？

我们利用一下CGLIB提供的工具，把动态生成的代理类打印出来看看。

```java
package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import net.sf.cglib.core.DebuggingClassWriter;

/**
 * <p> test </p>
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
        proxy.buyTickets();
    }
}
```

执行完后，在我们指定的目录下生成了以下目录和文件：

```verilog
resources
--com.lthaoshao.pattern.proxy.dynamicproxy.cglib
------Customer$$EnhancerByCGLIB$$6ec9c1ce$$FastClassByCGLIB$$8a48b4bd.class
------Customer$$EnhancerByCGLIB$$6ec9c1ce.class
------Customer$$FastClassByCGLIB$$4c63083e.class

--net.sf.cglib
----core
------MethodWrapper$MethodWrapperKey$$KeyFactoryByCGLIB$$d45e49f7.class
----proxy
------Enhancer$EnhancerKey$$KeyFactoryByCGLIB$$7fb24d72.class
```

我们通过JAD反编译后进行追踪发现，`Customer$$EnhancerByCGLIB$$6ec9c1ce.class`就是CGLIB生成的代理类。它继承了我们的Customer类，如下：

```java
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   <generated>

package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import java.lang.reflect.Method;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.*;

// Referenced classes of package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb:
//            Customer

public class Customer$$EnhancerByCGLIB$$6ec9c1ce extends Customer
    implements Factory
{

    static void CGLIB$STATICHOOK1()
    {
        Method amethod[];
        Method amethod1[];
        CGLIB$THREAD_CALLBACKS = new ThreadLocal();
        CGLIB$emptyArgs = new Object[0];
        Class class1 = Class.forName("com.lthaoshao.pattern.proxy.dynamicproxy.cglilb.Customer$$EnhancerByCGLIB$$6ec9c1ce");
        Class class2;
        amethod = ReflectUtils.findMethods(new String[] {
            "equals", "(Ljava/lang/Object;)Z", "toString", "()Ljava/lang/String;", "hashCode", "()I", "clone", "()Ljava/lang/Object;"
        }, (class2 = Class.forName("java.lang.Object")).getDeclaredMethods());
        Method[] _tmp = amethod;
        CGLIB$equals$1$Method = amethod[0];
        CGLIB$equals$1$Proxy = MethodProxy.create(class2, class1, "(Ljava/lang/Object;)Z", "equals", "CGLIB$equals$1");
        CGLIB$toString$2$Method = amethod[1];
        CGLIB$toString$2$Proxy = MethodProxy.create(class2, class1, "()Ljava/lang/String;", "toString", "CGLIB$toString$2");
        CGLIB$hashCode$3$Method = amethod[2];
        CGLIB$hashCode$3$Proxy = MethodProxy.create(class2, class1, "()I", "hashCode", "CGLIB$hashCode$3");
        CGLIB$clone$4$Method = amethod[3];
        CGLIB$clone$4$Proxy = MethodProxy.create(class2, class1, "()Ljava/lang/Object;", "clone", "CGLIB$clone$4");
        amethod1 = ReflectUtils.findMethods(new String[] {
            "buyTickets", "()V"
        }, (class2 = Class.forName("com.lthaoshao.pattern.proxy.dynamicproxy.cglilb.Customer")).getDeclaredMethods());
        Method[] _tmp1 = amethod1;
        CGLIB$buyTickets$0$Method = amethod1[0];
        CGLIB$buyTickets$0$Proxy = MethodProxy.create(class2, class1, "()V", "buyTickets", "CGLIB$buyTickets$0");
    }

    final void CGLIB$buyTickets$0()
    {
        super.buyTickets();
    }

    public final void buyTickets()
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 37;
           goto _L3 _L4
_L3:
        break MISSING_BLOCK_LABEL_21;
_L4:
        break MISSING_BLOCK_LABEL_37;
        this;
        CGLIB$buyTickets$0$Method;
        CGLIB$emptyArgs;
        CGLIB$buyTickets$0$Proxy;
        intercept();
        return;
        super.buyTickets();
        return;
    }

    final boolean CGLIB$equals$1(Object obj)
    {
        return super.equals(obj);
    }

    public final boolean equals(Object obj)
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 57;
           goto _L3 _L4
_L3:
        this;
        CGLIB$equals$1$Method;
        new Object[] {
            obj
        };
        CGLIB$equals$1$Proxy;
        intercept();
        JVM INSTR dup ;
        JVM INSTR ifnonnull 50;
           goto _L5 _L6
_L5:
        JVM INSTR pop ;
        false;
          goto _L7
_L6:
        (Boolean);
        booleanValue();
_L7:
        return;
_L4:
        return super.equals(obj);
    }

    final String CGLIB$toString$2()
    {
        return super.toString();
    }

    public final String toString()
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 40;
           goto _L3 _L4
_L3:
        this;
        CGLIB$toString$2$Method;
        CGLIB$emptyArgs;
        CGLIB$toString$2$Proxy;
        intercept();
        (String);
        return;
_L4:
        return super.toString();
    }

    final int CGLIB$hashCode$3()
    {
        return super.hashCode();
    }

    public final int hashCode()
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 52;
           goto _L3 _L4
_L3:
        this;
        CGLIB$hashCode$3$Method;
        CGLIB$emptyArgs;
        CGLIB$hashCode$3$Proxy;
        intercept();
        JVM INSTR dup ;
        JVM INSTR ifnonnull 45;
           goto _L5 _L6
_L5:
        JVM INSTR pop ;
        0;
          goto _L7
_L6:
        (Number);
        intValue();
_L7:
        return;
_L4:
        return super.hashCode();
    }

    final Object CGLIB$clone$4()
        throws CloneNotSupportedException
    {
        return super.clone();
    }

    protected final Object clone()
        throws CloneNotSupportedException
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 37;
           goto _L3 _L4
_L3:
        this;
        CGLIB$clone$4$Method;
        CGLIB$emptyArgs;
        CGLIB$clone$4$Proxy;
        intercept();
        return;
_L4:
        return super.clone();
    }

    public static MethodProxy CGLIB$findMethodProxy(Signature signature)
    {
        String s = signature.toString();
        s;
        s.hashCode();
        JVM INSTR lookupswitch 5: default 120
    //                   -508378822: 60
    //                   635195988: 72
    //                   1826985398: 84
    //                   1913648695: 96
    //                   1984935277: 108;
           goto _L1 _L2 _L3 _L4 _L5 _L6
_L2:
        "clone()Ljava/lang/Object;";
        equals();
        JVM INSTR ifeq 121;
           goto _L7 _L8
_L8:
        break MISSING_BLOCK_LABEL_121;
_L7:
        return CGLIB$clone$4$Proxy;
_L3:
        "buyTickets()V";
        equals();
        JVM INSTR ifeq 121;
           goto _L9 _L10
_L10:
        break MISSING_BLOCK_LABEL_121;
_L9:
        return CGLIB$buyTickets$0$Proxy;
_L4:
        "equals(Ljava/lang/Object;)Z";
        equals();
        JVM INSTR ifeq 121;
           goto _L11 _L12
_L12:
        break MISSING_BLOCK_LABEL_121;
_L11:
        return CGLIB$equals$1$Proxy;
_L5:
        "toString()Ljava/lang/String;";
        equals();
        JVM INSTR ifeq 121;
           goto _L13 _L14
_L14:
        break MISSING_BLOCK_LABEL_121;
_L13:
        return CGLIB$toString$2$Proxy;
_L6:
        "hashCode()I";
        equals();
        JVM INSTR ifeq 121;
           goto _L15 _L16
_L16:
        break MISSING_BLOCK_LABEL_121;
_L15:
        return CGLIB$hashCode$3$Proxy;
_L1:
        JVM INSTR pop ;
        return null;
    }

    public static void CGLIB$SET_THREAD_CALLBACKS(Callback acallback[])
    {
        CGLIB$THREAD_CALLBACKS.set(acallback);
    }

    public static void CGLIB$SET_STATIC_CALLBACKS(Callback acallback[])
    {
        CGLIB$STATIC_CALLBACKS = acallback;
    }

    private static final void CGLIB$BIND_CALLBACKS(Object obj)
    {
        Customer$$EnhancerByCGLIB$$6ec9c1ce customer$$enhancerbycglib$$6ec9c1ce = (Customer$$EnhancerByCGLIB$$6ec9c1ce)obj;
        if(customer$$enhancerbycglib$$6ec9c1ce.CGLIB$BOUND) goto _L2; else goto _L1
_L1:
        Object obj1;
        customer$$enhancerbycglib$$6ec9c1ce.CGLIB$BOUND = true;
        obj1 = CGLIB$THREAD_CALLBACKS.get();
        obj1;
        if(obj1 != null) goto _L4; else goto _L3
_L3:
        JVM INSTR pop ;
        CGLIB$STATIC_CALLBACKS;
        if(CGLIB$STATIC_CALLBACKS != null) goto _L4; else goto _L5
_L5:
        JVM INSTR pop ;
          goto _L2
_L4:
        (Callback[]);
        customer$$enhancerbycglib$$6ec9c1ce;
        JVM INSTR swap ;
        0;
        JVM INSTR aaload ;
        (MethodInterceptor);
        CGLIB$CALLBACK_0;
_L2:
    }

    public Object newInstance(Callback acallback[])
    {
        CGLIB$SET_THREAD_CALLBACKS(acallback);
        CGLIB$SET_THREAD_CALLBACKS(null);
        return new Customer$$EnhancerByCGLIB$$6ec9c1ce();
    }

    public Object newInstance(Callback callback)
    {
        CGLIB$SET_THREAD_CALLBACKS(new Callback[] {
            callback
        });
        CGLIB$SET_THREAD_CALLBACKS(null);
        return new Customer$$EnhancerByCGLIB$$6ec9c1ce();
    }

    public Object newInstance(Class aclass[], Object aobj[], Callback acallback[])
    {
        CGLIB$SET_THREAD_CALLBACKS(acallback);
        JVM INSTR new #2   <Class Customer$$EnhancerByCGLIB$$6ec9c1ce>;
        JVM INSTR dup ;
        aclass;
        aclass.length;
        JVM INSTR tableswitch 0 0: default 35
    //                   0 28;
           goto _L1 _L2
_L2:
        JVM INSTR pop ;
        Customer$$EnhancerByCGLIB$$6ec9c1ce();
          goto _L3
_L1:
        JVM INSTR pop ;
        throw new IllegalArgumentException("Constructor not found");
_L3:
        CGLIB$SET_THREAD_CALLBACKS(null);
        return;
    }

    public Callback getCallback(int i)
    {
        CGLIB$BIND_CALLBACKS(this);
        this;
        i;
        JVM INSTR tableswitch 0 0: default 30
    //                   0 24;
           goto _L1 _L2
_L2:
        CGLIB$CALLBACK_0;
          goto _L3
_L1:
        JVM INSTR pop ;
        null;
_L3:
        return;
    }

    public void setCallback(int i, Callback callback)
    {
        switch(i)
        {
        case 0: // '\0'
            CGLIB$CALLBACK_0 = (MethodInterceptor)callback;
            break;
        }
    }

    public Callback[] getCallbacks()
    {
        CGLIB$BIND_CALLBACKS(this);
        this;
        return (new Callback[] {
            CGLIB$CALLBACK_0
        });
    }

    public void setCallbacks(Callback acallback[])
    {
        this;
        acallback;
        JVM INSTR dup2 ;
        0;
        JVM INSTR aaload ;
        (MethodInterceptor);
        CGLIB$CALLBACK_0;
    }

    private boolean CGLIB$BOUND;
    public static Object CGLIB$FACTORY_DATA;
    private static final ThreadLocal CGLIB$THREAD_CALLBACKS;
    private static final Callback CGLIB$STATIC_CALLBACKS[];
    private MethodInterceptor CGLIB$CALLBACK_0;
    private static Object CGLIB$CALLBACK_FILTER;
    private static final Method CGLIB$buyTickets$0$Method;
    private static final MethodProxy CGLIB$buyTickets$0$Proxy;
    private static final Object CGLIB$emptyArgs[];
    private static final Method CGLIB$equals$1$Method;
    private static final MethodProxy CGLIB$equals$1$Proxy;
    private static final Method CGLIB$toString$2$Method;
    private static final MethodProxy CGLIB$toString$2$Proxy;
    private static final Method CGLIB$hashCode$3$Method;
    private static final MethodProxy CGLIB$hashCode$3$Proxy;
    private static final Method CGLIB$clone$4$Method;
    private static final MethodProxy CGLIB$clone$4$Proxy;

    static 
    {
        CGLIB$STATICHOOK1();
    }

    public Customer$$EnhancerByCGLIB$$6ec9c1ce()
    {
        CGLIB$BIND_CALLBACKS(this);
    }
}
```

重写了 Customer 类的所有方法。我们通过代理类的源码可以看到，代理类会获得所有在 父 类 继 承 来 的 方 法， 并 且 会 有 MethodProxy 与 之 对 应 ， 比 如`Method CGLIB$buyTickets$0$Method`、`MethodProxy CGLIB$buyTickets$0$Proxy`等等，这些方法在代理类的`buyTickets`中都有调用。

```java
// 代理方法 （methodProxy.invokeSuper 会调用）   
	final void CGLIB$buyTickets$0()
    {
        super.buyTickets();
    }

//被代理方法(methodProxy.invoke 会调用，这就是为什么在拦截器中调用 methodProxy.invoke 会死循环，一直在调
用拦截器)
    public final void buyTickets()
    {
        CGLIB$CALLBACK_0;
        if(CGLIB$CALLBACK_0 != null) goto _L2; else goto _L1
_L1:
        JVM INSTR pop ;
        CGLIB$BIND_CALLBACKS(this);
        CGLIB$CALLBACK_0;
_L2:
        JVM INSTR dup ;
        JVM INSTR ifnull 37;
           goto _L3 _L4
_L3:
        break MISSING_BLOCK_LABEL_21;
_L4:
        break MISSING_BLOCK_LABEL_37;
        this;
        CGLIB$buyTickets$0$Method;
        CGLIB$emptyArgs;
        CGLIB$buyTickets$0$Proxy;
    	// 调用拦截器
        intercept();
        return;
        super.buyTickets();
        return;
    }
```

调用过程：

代理对象调用 this.buyTickets()方法 --> 调用拦截器 --> methodProxy.invokeSuper() --> CGLIB$buyTickets$0 --> >被代理对象 buyTickets()方法。

此时，我们发现拦截器 MethodInterceptor 中就是由 MethodProxy 的 invokeSuper 方法调用代理方法的，MethodProxy 非常关键，我们分析一下它具体做了什么。 

```java
/*
 * Copyright 2003,2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.cglib.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.GeneratorStrategy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Signature;
import net.sf.cglib.reflect.FastClass;

public class MethodProxy {
    private Signature sig1;
    private Signature sig2;
    private CreateInfo createInfo;
    
    private final Object initLock = new Object();
    private volatile FastClassInfo fastClassInfo;
    
    public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2) {
        MethodProxy proxy = new MethodProxy();
        proxy.sig1 = new Signature(name1, desc);
        proxy.sig2 = new Signature(name2, desc);
        proxy.createInfo = new CreateInfo(c1, c2);
        return proxy;
    }

    // MethodProxy invoke()/invokeSuper() 都调用了 init()
    private void init()
    {
        if (fastClassInfo == null)
        {
            synchronized (initLock)
            {
                if (fastClassInfo == null)
                {
                    CreateInfo ci = createInfo;

                    FastClassInfo fci = new FastClassInfo();
                    // 如果缓存中就取出，没有就生成新的 FastClass
                    fci.f1 = helper(ci, ci.c1);
                    fci.f2 = helper(ci, ci.c2);
                    // 获取方法的 index
                    fci.i1 = fci.f1.getIndex(sig1);
                    fci.i2 = fci.f2.getIndex(sig2);
                    fastClassInfo = fci;
                    createInfo = null;
                }
            }
        }
    }

    private static class FastClassInfo
    {
        FastClass f1;
        FastClass f2;
        int i1;
        int i2;
    }

    private static class CreateInfo
    {
        Class c1;
        Class c2;
        NamingPolicy namingPolicy;
        GeneratorStrategy strategy;
        boolean attemptLoad;
        
        public CreateInfo(Class c1, Class c2)
        {
            this.c1 = c1;
            this.c2 = c2;
            AbstractClassGenerator fromEnhancer = AbstractClassGenerator.getCurrent();
            if (fromEnhancer != null) {
                namingPolicy = fromEnhancer.getNamingPolicy();
                strategy = fromEnhancer.getStrategy();
                attemptLoad = fromEnhancer.getAttemptLoad();
            }
        }
    }

    private static FastClass helper(CreateInfo ci, Class type) {
        FastClass.Generator g = new FastClass.Generator();
        g.setType(type);
        g.setClassLoader(ci.c2.getClassLoader());
        g.setNamingPolicy(ci.namingPolicy);
        g.setStrategy(ci.strategy);
        g.setAttemptLoad(ci.attemptLoad);
        return g.create();
    }

    private MethodProxy() {
    }

    public Signature getSignature() {
        return sig1;
    }

    public String getSuperName() {
        return sig2.getName();
    }

    public int getSuperIndex() {
        init();
        return fastClassInfo.i2;
    }

    // For testing
    FastClass getFastClass() {
      init();
      return fastClassInfo.f1;
    }

    // For testing
    FastClass getSuperFastClass() {
      init();
      return fastClassInfo.f2;
    }

    public static MethodProxy find(Class type, Signature sig) {
        try {
            Method m = type.getDeclaredMethod(MethodInterceptorGenerator.FIND_PROXY_NAME,
                                              MethodInterceptorGenerator.FIND_PROXY_TYPES);
            return (MethodProxy)m.invoke(null, new Object[]{ sig });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + type + " does not use a MethodInterceptor");
        } catch (IllegalAccessException e) {
            throw new CodeGenerationException(e);
        } catch (InvocationTargetException e) {
            throw new CodeGenerationException(e);
        }
    }

    public Object invoke(Object obj, Object[] args) throws Throwable {
        try {
            init();
            FastClassInfo fci = fastClassInfo;
            return fci.f1.invoke(fci.i1, obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalArgumentException e) {
            if (fastClassInfo.i1 < 0)
                throw new IllegalArgumentException("Protected method: " + sig1);
            throw e;
        }
    }

    public Object invokeSuper(Object obj, Object[] args) throws Throwable {
        try {
            init();
            FastClassInfo fci = fastClassInfo;
            return fci.f2.invoke(fci.i2, obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}

```

调用invokeSuper() --> init() --> FastClassInfo --> 后面就是在调用FastClass的invoke()方法了。还记得之前生成的文件中有个`Customer$$EnhancerByCGLIB$$6ec9c1ce$$FastClassByCGLIB$$8a48b4bd.class`它就是代理类的Fast类。

CGLIB动态代理的执行效率之所以比JDK高，就是因为它采用了FastClass机制。它的原理就是：为代理类和被代理类各生成一个Class，这个Class会为代理类和被代理类分配各分配一个index（int类型）。这个index当做一个参数传入，FastClass就可以直接定位到要调用的方法并直接调用，省去了反射调用的过程，所以它的执行效率要比JDK动态代理的反射调用执行效率高。

FastClass的反编译结果如下：

```java
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   <generated>

package com.lthaoshao.pattern.proxy.dynamicproxy.cglilb;

import java.lang.reflect.InvocationTargetException;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.reflect.FastClass;

public class Customer$$EnhancerByCGLIB$$6ec9c1ce$$FastClassByCGLIB$$8a48b4bd extends FastClass
{

    public int getIndex(Signature signature)
    {
        String s = signature.toString();
        s;
        s.hashCode();
        JVM INSTR lookupswitch 21: default 413
    //                   -2055565910: 188
    //                   -1882565338: 199
    //                   -1457535688: 210
    //                   -1411842725: 221
    //                   -894172689: 232
    //                   -623122092: 242
    //                   -508378822: 253
    //                   -419626537: 263
    //                   560567118: 274
    //                   635195988: 285
    //                   811063227: 296
    //                   973717575: 307
    //                   1221173700: 318
    //                   1230699260: 328
    //                   1245895525: 339
    //                   1306468936: 350
    //                   1584330438: 361
    //                   1800494055: 372
    //                   1826985398: 383
    //                   1913648695: 393
    //                   1984935277: 403;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L22
_L2:
        "CGLIB$SET_THREAD_CALLBACKS([Lnet/sf/cglib/proxy/Callback;)V";
        equals();
        JVM INSTR ifeq 414;
           goto _L23 _L24
_L24:
        break MISSING_BLOCK_LABEL_414;
_L23:
        return 10;
_L3:
        "CGLIB$equals$1(Ljava/lang/Object;)Z";
        equals();
        JVM INSTR ifeq 414;
           goto _L25 _L26
_L26:
        break MISSING_BLOCK_LABEL_414;
_L25:
        return 19;
_L4:
        "CGLIB$STATICHOOK1()V";
        equals();
        JVM INSTR ifeq 414;
           goto _L27 _L28
_L28:
        break MISSING_BLOCK_LABEL_414;
_L27:
        return 15;
        
//...省略部分代码

    // 根据 index 直接定位执行方法
    public Object invoke(int i, Object obj, Object aobj[])
        throws InvocationTargetException
    {
        (Customer..EnhancerByCGLIB.._cls6ec9c1ce)obj;
        i;
        JVM INSTR tableswitch 0 20: default 311
    //                   0 104
    //                   1 119
    //                   2 123
    //                   3 135
    //                   4 139
    //                   5 149
    //                   6 159
    //                   7 181
    //                   8 201
    //                   9 206
    //                   10 217
    //                   11 228
    //                   12 241
    //                   13 245
    //                   14 256
    //                   15 266
    //                   16 271
    //                   17 276
    //                   18 288
    //                   19 292
    //                   20 307;
           goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L22
_L2:
        aobj[0];
        equals();
        JVM INSTR new #138 <Class Boolean>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        Boolean();
        return;
_L3:
        toString();
        return;
_L4:
        hashCode();
        JVM INSTR new #145 <Class Integer>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        Integer();
        return;
_L5:
        clone();
        return;
_L6:
        (Callback[])aobj[0];
        newInstance();
        return;
_L7:
        (Callback)aobj[0];
        newInstance();
        return;
_L8:
        (Class[])aobj[0];
        (Object[])aobj[1];
        (Callback[])aobj[2];
        newInstance();
        return;
_L9:
        ((Number)aobj[0]).intValue();
        (Callback)aobj[1];
        setCallback();
        return null;
_L10:
        buyTickets();
        return null;
_L11:
        Customer..EnhancerByCGLIB.._cls6ec9c1ce.CGLIB$SET_STATIC_CALLBACKS((Callback[])aobj[0]);
        return null;
_L12:
        Customer..EnhancerByCGLIB.._cls6ec9c1ce.CGLIB$SET_THREAD_CALLBACKS((Callback[])aobj[0]);
        return null;
_L13:
        ((Number)aobj[0]).intValue();
        getCallback();
        return;
_L14:
        getCallbacks();
        return;
_L15:
        (Callback[])aobj[0];
        setCallbacks();
        return null;
_L16:
        return Customer..EnhancerByCGLIB.._cls6ec9c1ce.CGLIB$findMethodProxy((Signature)aobj[0]);
_L17:
        Customer..EnhancerByCGLIB.._cls6ec9c1ce.CGLIB$STATICHOOK1();
        return null;
_L18:
        CGLIB$buyTickets$0();
        return null;
_L19:
        CGLIB$hashCode$3();
        JVM INSTR new #145 <Class Integer>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        Integer();
        return;
_L20:
        CGLIB$clone$4();
        return;
_L21:
        aobj[0];
        CGLIB$equals$1();
        JVM INSTR new #138 <Class Boolean>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        Boolean();
        return;
_L22:
        CGLIB$toString$2();
        return;
        JVM INSTR new #133 <Class InvocationTargetException>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        InvocationTargetException();
        throw ;
_L1:
        throw new IllegalArgumentException("Cannot find matching method/constructor");
    }

    public Object newInstance(int i, Object aobj[])
        throws InvocationTargetException
    {
        JVM INSTR new #135 <Class Customer$$EnhancerByCGLIB$$6ec9c1ce>;
        JVM INSTR dup ;
        i;
        JVM INSTR tableswitch 0 0: default 28
    //                   0 24;
           goto _L1 _L2
_L2:
        Customer..EnhancerByCGLIB.._cls6ec9c1ce();
        return;
        JVM INSTR new #133 <Class InvocationTargetException>;
        JVM INSTR dup_x1 ;
        JVM INSTR swap ;
        InvocationTargetException();
        throw ;
_L1:
        throw new IllegalArgumentException("Cannot find matching method/constructor");
    }

    public int getMaxIndex()
    {
        return 20;
    }

    public Customer$$EnhancerByCGLIB$$6ec9c1ce$$FastClassByCGLIB$$8a48b4bd(Class class1)
    {
        super(class1);
    }
}
```

需要注意的是，FastClass并不是跟代理类一起生成的， 而是在第一次执行 MethodProxy.invoke/invokeSuper 时生成的并放在了缓存中。MethodProxy.invoke/invokeSuper都调用了MethodProxy.init()，并优先从缓存获取，缓存中不存在时就生成并放入缓存。



### DBRoute改版

#### JDK动态代理版本

下面我们使用JDK动态代理来对我们的数据源路由进行改造。

```java
package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> JDK动态代理版本 </p>
 *
 * @author lijinghao
 * @version : OrderServiceDynamicProxy.java, v 0.1 2019年08月01日 20:34:34 lijinghao Exp $
 */
public class OrderServiceDynamicProxyByJDK implements InvocationHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doBefore(args[0]);
        String result = (String) method.invoke(target, args);
        doAfter();
        return result;
    }

    private void doAfter() {
        System.out.println("Proxy after method");
        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
    }

    private void doBefore(Object target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Proxy before method");

        Date time = (Date) target.getClass().getMethod("getCreateTime").invoke(target);
        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_" + sdf.format(time));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());
    }

}
```

测试类：

```java
package com.lthaoshao.pattern.proxy.dbroute;

import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByJDK;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p> test </p>
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
        // IOrderService service = new OrderServiceStaticProxy(orderService);
        OrderServiceDynamicProxyByJDK orderProxy = new OrderServiceDynamicProxyByJDK();
        IOrderService service = (IOrderService)orderProxy.getInstance(orderService);
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

结果是OK的。

#### CGLIB动态代理版本

下面来使用CGLIB动态代理实现。

```java
package com.lthaoshao.pattern.proxy.dbroute.proxy;

import com.lthaoshao.pattern.proxy.dbroute.DynamicDataSourceEntry;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> 基于CGLIB动态代理 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/10 9:30
 */
public class OrderServiceDynamicProxyByCglib implements MethodInterceptor {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    private Object target;
    public Object getInstance(Object o) {
        this.target = o;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(o.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before(args[0]);
        Object o = proxy.invokeSuper(obj, args);
        after();
        return o;
    }

    private void after() {
        System.out.println("CGLIB 动态代理 执行后操作");
        DynamicDataSourceEntry.restore();
        System.out.println("重置数据源到:" + DynamicDataSourceEntry.get());
    }

    private void before(Object target) {
        System.out.println("CGLIB 动态代理 执行前操作");
        Date time = null;
        try {
            time = (Date) target.getClass().getMethod("getCreateTime").invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 现在在调用服务之前设置数据源
        DynamicDataSourceEntry.set("DB_" + sdf.format(time));
        System.out.println("自动分配数据源到:" + DynamicDataSourceEntry.get());
    }
}
```

测试代码：

```java
package com.lthaoshao.pattern.proxy.dbroute;

import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByCglib;
import com.lthaoshao.pattern.proxy.dbroute.proxy.OrderServiceDynamicProxyByJDK;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p> test </p>
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
        // IOrderService service = new OrderServiceStaticProxy(orderService);
        // OrderServiceDynamicProxyByJDK orderProxy = new OrderServiceDynamicProxyByJDK();
        OrderServiceDynamicProxyByCglib orderProxy = new OrderServiceDynamicProxyByCglib();
        IOrderService service = (IOrderService)orderProxy.getInstance(orderService);
        String result = service.createOrder(order);
        System.out.println(result);
    }
}
```

执行结果：

```verilog
CGLIB 动态代理 执行前操作
自动分配数据源到:DB_2017
OrderService调用createOrder完成创建订单
完成订单插入操作
CGLIB 动态代理 执行后操作
重置数据源到:null
完成创建订单

Process finished with exit code 0
```

结果同样是OK的。



### 总结

#### JDK和CGLIB动态代理对比

1、JDK动态代理是实现类被代理对象的相同的接口，CGLIB是继承了被代理对象；

2、JDK和CGLIB都是在运行时生成字节码，JDK是直接写字节码，CGLIB是采用了ASM框架写字节码，CGLIB代理的实现更加复杂，生成代理类的效率比JDK要低；

3、JDK调用代理方法，是通过反射机制调用的，而CGLIB采用了FastClass机制，直接调用代理方法，也因此CGLIB的执行效率更高；

#### Spring中的动态代理

Spring中使用到设计模式的类的命名有个特点，就是以设计模式的名字开头或结尾。

代理模式的应用，先看一下`org.springframework.aop.framework.ProxyFactoryBean，它的核心方法就是getObject()。

```java
@Override
@Nullable
public Object getObject() throws BeansException {
    initializeAdvisorChain();
    if (isSingleton()) {
        return getSingletonInstance();
    }
    else {
        if (this.targetName == null) {
            logger.info("Using non-singleton proxies with singleton targets is often undesirable. " +
                        "Enable prototype proxies by setting the 'targetName' property.");
        }
        return newPrototypeInstance();
    }
}
```

在getObject()方法中，主要的就是getSingletonInstance()和newPrototypeInstance()。

在Spring中，如果不进行任何配置，默认的Bean都是单例的，如果修改Bean的scope为prototype，则每次都会创建一个新的原型对象。

Spring 利用动态代理实现 AOP 有两个非常重要的类，一个是 JdkDynamicAopProxy 类 和 CglibAopProxy 类，来看一下类图：

![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-proxy/src/main/resources/AopProxy.png?raw=true "AOP")

**Spring 中的代理选择原则** 

1、当 Bean 有实现接口时，Spring 就会用 JDK 的动态代理 ；

2、当 Bean 没有实现接口时，Spring 选择 CGLIB动态代理；

3、Spring 可以通过配置强制使用 CGLib，只需在 Spring 的配置文件中加入如下代码：

```xml
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

#### 静态代理和动态的本质区别

1、静态代理类只能通过手动完成代理操作，如果被代理对象增加新的方法，代理类需要同步新增，违背了开闭原则；

2、动态代理采用在运行时生成代理对象字节码的方式，取消了对被代理对象 扩展限制，遵循开闭原则；

3、若动态代理要对目标类的增强逻辑扩展，结合策略模式，只需要新增策略类便可完成，无需修改代理类的代码；

#### 代理模式的优缺点

**优点：**

1、代理模式能将被代理对象与真实调用的目标对象进行分离；

2、一定程度上降低了系统的耦合性，扩展性好；

3、可以起到保护目标对象的作用；

4、可以对目标对象进行增强；

**缺点：**

1、代理模式会造成系统设计中类的个数增加；

2、在客户端和目标对象之间增加一个代理类，会造成请求处理变慢，当然基本无感知；

3、增加了系统的复杂度；