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





书写代理类, 编译成字节码文件, 并加载到内存中;

### DBRoute改版

### 总结

保存代理类字节码文件, 并进行反编译查看内容;