# [设计模式](../README.md)

## 单例模式(Singleton Pattern)
### 概念:
单例模式（Singleton Pattern）是 Java 中最简单的设计模式之一。这种类型的设计模式属于创建型模式，它提供了一种创建对象的最佳方式。
这种模式涉及到一个单一的类，该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。

### 关键点:
* 确保一个类在任何情况下只存在一个实例;
* 私有化构造方法, 由该类负责创建自己的实例;
* 对外提供全局获取该实例的方法

### 单例模式的优缺点:
#### 优点:
* 在内存中至多只存在一个实例, 减少了内存的开销, 尤其是频繁的创建和销毁实例;
* 避免对资源的多重占用;

#### 缺点:
* 没有接口, 不能继承; 扩展困难, 要扩展就必须要修改代码, 不符合开闭原则;

### 应用场景
单例模式在现实生活中应用也非常广泛。
例如，国家主席、公司 CEO、部门经理等。在 J2EE 标准中，ServletContext, ServletContextConfig 等; 在 Spring 框架应用中 ApplicationContext, 数据库的连接池也都是单例形式.

### UML
![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/src/main/resources/com.lthaoshao.pattern.singleton/singleton.png "简单工厂模式")

### 1. 饿汉式单例

饿汉式是在类加载的时候就初始化, 并创建单例对象. 线程绝对安全, 在现场还未出现以前就实例化了, 不可能存在线程安全问题.

```java
public class HungrySingleton {
    /**
     * 私有化构造方法
     */
    private HungrySingleton() {}
    // finale 是为了防止反射破坏单例
    private static final HungrySingleton INSTANCE = new HungrySingleton();
    /**
     * 提供统一的对外访问路径
     *
      * @return
     */
    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
```
另一种写法就是把创建实例的过程放到静态代码块中进行, 效果是一样的.

#### 饿汉式单例的优缺点:
    优点:
        1) 绝对线程安全, 在线程还没出现前就进行了初始化.
        2) 没有加任何锁, 执行效率高;
        3) 在用户体验上比懒汉式要好;
    缺点:
        1) 在类加载的时候就完成了初始化, 无论是否使用, 都占用了空间, 浪费内存.

### 2. 懒汉式单例
懒汉式单例, 如其名-懒, 需要等到调用的时候才会加载实例; 这样做节省了内存, 但需要注意线程安全问题.

* 简单实现模式
```java
public class SimpleLazySingleton {
    private SimpleLazySingleton() { }
    private static SimpleLazySingleton instance;
    public synchronized static SimpleLazySingleton getInstance() {
        if (instance == null) {
            instance = new SimpleLazySingleton();
        }
        return instance;
    }
}
```
如上代码, 懒汉式单例的实例并没有一开始就创建, 而是等到客户端调用 getInstance()方法时, 再跟进是否已经创建实例来决定是否创建实例.
这里使用了'synchronized'来保证线程的安全, 'synchronized'在JDK1.8以后底层做了很多优化,性能也提升了不少. 
由于 synchronized 加在了静态方法上, 所以采用的是类级别锁, 对性能有一定影响.

* 双重校验实现模式
```java
public class DoubleCheckLazySingleton {
    private DoubleCheckLazySingleton() {}
    private volatile static DoubleCheckLazySingleton instance;
    public static DoubleCheckLazySingleton getInstance() {
        // 使得使用synchronized的概率更小了, 效率更高了
        if (instance == null) {
            // 保证只有一个线程创建实例
            synchronized (DoubleCheckLazySingleton.class) {
                // 再次校验实例是否已存在
                if (instance == null) {
                    instance = new DoubleCheckLazySingleton();
                }
            }
        }
        return instance;
    }
}
```
双重校验模式, 将锁的范围进一步缩小, 提高了执行效率.
但我们知道, 在初始化的工程中, 是将字节码文件转成操作系统指令完成的, 
这个步骤中, CPU可能会对指令进行优化, 如,指令重排序.

重排序是指编译器和处理器为了优化程序性能而对指令序列进行重新排序的一种手段
synchronized 关键字在被编译成操作指令时, 有可能存在指令重排序, 如:

>instance = new DoubleCheckLazySingleton();

这句话会分为以下几个步骤:

    memory = allocate;       // 1 为对象分配空间;
    ctroInstance(memory);    // 2 初始化对象
    instance = memory;       // 3 将instance指向刚分配的内存
    
关键点在于步骤 2 和 3 之间, 可能会发生重排序.

    memory = allocate;       // 1 为对象分配空间;
    instance = memory;       // 3 将instance指向刚分配的内存
    // 注意: 此时还没有完成对象的初始化
    ctroInstance(memory);    // 2 初始化对象

这在单线程下是没有任何问题的, 但在多线程下, 就会存在一定问题.

假设现在存在两个线程 A 和 B, 字节码发生了重排序变成了步骤 1-3-2.
设当线程A 先执行到步骤3(此时还未执行步骤2, 即对象尚未初始化), 
这时线程A挂起, 线程B来了, 判断instance != null, 就直接将这个实例返回了,
但问题是现在的instance尚未被初始化, 这就导致了在B线程中使用了未初始化的实例.

所以一个完成的双重校验懒汉式, volatile 关键字还是必要的,他的作用就是包装内存可见, 禁止CPU指令重排序.

* 静态内部类模式
```java
public class InnerClassLazySingleton {
    private InnerClassLazySingleton(){}
    // final 保证方法不会被重写,重载
    public static final InnerClassLazySingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
    /**
     * 静态内部类
     * private是为了不让其他的类访问
     */
    private static class SingletonHolder{
        private static final InnerClassLazySingleton INSTANCE = new InnerClassLazySingleton();
    }
}
```
这种形式的单例兼顾了饿汉式的内存浪费, 也兼顾了synchronized的性能问题. 完美地屏蔽了这两个缺点.
号称最牛逼的单例实现方式.它利用了静态内部类的加载机制, 静态内部类一定要在被调用之前进行初始化, 
巧妙地避开了线程安全问题.

#### 懒汉式单例的优缺点:
    
    优点:
        1)延迟实例化, 不使用不实例化, 节省内存空间;
    缺点:
        1)为了包装线程安全, 采用了锁机制, 性能会有所下降;


### 3. 注册式单例
将每一个实例都缓存到统一的容器中，使用唯一标识获取实例;

* 枚举式单例
```java
public enum EnumSingleton {
    /**
     * 枚举值
     */
    INSTANCE;

    /**
     * 数据
     */
    private Object data;
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
```
枚举是我们开发中很常见的一种形式, 实际上枚举就是一种注册式的单例模式.
如代码中所示, 设置唯一的实例 INSTANCE, 将 data 做为数据存储. 
我们来看一下反编译后的文件(通过JDA反编译):
```java
package com.lthaoshao.pattern.singleton.register;


public final class EnumSingleton extends Enum
{

    public static EnumSingleton[] values()
    {
        return (EnumSingleton[])$VALUES.clone();
    }

    public static EnumSingleton valueOf(String name)
    {
        return (EnumSingleton)Enum.valueOf(com/lthaoshao/pattern/singleton/register/EnumSingleton, name);
    }

    private EnumSingleton(String s, int i)
    {
        super(s, i);
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public static EnumSingleton getInstance()
    {
        return INSTANCE;
    }

    public static final EnumSingleton INSTANCE;
    private Object data;
    private static final EnumSingleton $VALUES[];

    static 
    {
        INSTANCE = new EnumSingleton("INSTANCE", 0);
        $VALUES = (new EnumSingleton[] {
            INSTANCE
        });
    }
}
```
从上面代码中可以看出, 枚举也是属于懒加载的形式,
之所以称之为注册式, 是因为它的valueOf(Class<?> clazz, String name)方法.
我们平时调用的EnumSingleton.INSTANCE 其实就是在调用valueOf方法.
看一下它的调用过程.

```java
public abstract class Enum<E extends Enum<E>>
        implements Comparable<E>, Serializable {
    // ...简略一些代码
    // Enum.java 中的valueOf
    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        // 调用了Class的enumConstantDirectory, 这是一个Map
        T result = enumType.enumConstantDirectory().get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + "." + name);
    }
    
    // ... 省略代码
}
```
枚举类的Class对象中设置了存放枚举值的容器, 所以注册式单例也被称之为容器式单例.
下面为Class中的代码片段:
```java
public final class Class<T> implements java.io.Serializable,
                              GenericDeclaration,
                              Type,
                              AnnotatedElement {
    // ... 省略代码
                              
    Map<String, T> enumConstantDirectory() {
        if (enumConstantDirectory == null) {
            // 通过反射找到所有的枚举值
            T[] universe = getEnumConstantsShared();
            if (universe == null)
                throw new IllegalArgumentException(
                    getName() + " is not an enum type");
            
            // 可以看到, 枚举会存储到这个Map中
            Map<String, T> m = new HashMap<>(2 * universe.length);
            // 遍历枚举值, 放入map中. 以枚举值的名称为key, 枚举值本身为value
            for (T constant : universe)
                m.put(((Enum<?>)constant).name(), constant);
            enumConstantDirectory = m;
        }
        return enumConstantDirectory;
    }
    private volatile transient Map<String, T> enumConstantDirectory = null;

    // ... 省略代码
}
```
* 容器式单例

Spring的IOC其实就是使用的容器式单例.
```java
public class ContainerSingleton {
    private ContainerSingleton() {}
    private static final Map<String, Object> map = new ConcurrentHashMap<>();
    public static Object getInstance(String className) {
            synchronized (map) {
                if (!map.containsKey(className)) {
                    Object obj;
                    try {
                        Class<?> clazz = Class.forName(className);
                        obj = clazz.newInstance();
                        map.put(className, obj);
                        return obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        return map.get(className);
    }
}
```


### 4. ThreadLocal当前线程内单例
保证线程内部的全局唯一，且天生线程安全.
```java
public class ThreadLocalSingleton {
    private ThreadLocalSingleton() {}
    private static final ThreadLocal<ThreadLocalSingleton> INSTANCE =
            ThreadLocal.withInitial(ThreadLocalSingleton::new);
    public static ThreadLocalSingleton getInstance(){
        return INSTANCE.get();
    }
}
```

## 如何防止单例被破坏

### 1 通过反射破坏单例及如何防止

* 反射获取单例实例
```java
public class DestroySingletonByReflect {
    public static void main(String[] args){
        DoubleCheckLazySingleton singleton = DoubleCheckLazySingleton.getInstance();
        System.out.println(singleton);
        // 获取Class对象
        Class<?> clazz = DoubleCheckLazySingleton.class;
        // 通过反射获取构造, 一定要使用这个getDeclaredConstructor, 声明的构造
        Constructor<?> c = clazz.getDeclaredConstructor();
        // 设置暴力访问,私有的方法要设置true后才可以访问,否则会有IllegalAccessException异常
        c.setAccessible(true);    
        // 强制获取实例
        Object instance = c.newInstance();
        System.out.println(instance);
        // 结果为false说明不是一个实例, 则表明单例被破坏
        System.out.println(singleton == instance);
    } 
}
```
反射破坏单例, 是通过其构造创建的实例, 即使是私有化的, 有可以采用暴力方式进行访问.

* 解决办法

如可以在单例的私有构造中添加判断, 当不满足时抛出异常.
```java
private DoubleCheckLazySingleton() {
    if(instance != null){
        throw new RuntimeException("不可以实例化");
    }
}
```

### 2 通过序列化破坏单例及如何防止

* 序列化后通过反序列化来获取单例
```java
public class DestroySingletonBySerialized {
    public static void main(String[] args) {
        String path = "src/main/resources/com.lthaoshao.pattern.singleton/SerializeSingleton.obj";
        // 单例类需要实现序列化接口
        SerializeSingleton instance = SerializeSingleton.getInstance();

        // 序列化, 就是把内存中的状态, 通过转换成字节码的形式,从而转换成一个输出流, 写入到其他地方(磁盘/网络IO)
        // 内存中的状态就被永久保存下来了
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(instance);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 反序列化, 就是将已经持久化的字节码内容, 转换成IO流的形式, 通过IO流进行读取,
        // 进而将读取的内日转换成对象, 在转换的过程中会对对象重新new
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {

            // 读取实体类
            Object o = ois.readObject();

            // 这两个地址不等, 则表示破坏了单例
            System.out.println(instance);
            System.out.println(o);
            System.out.println(o == instance);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```

* 解决办法

为单例类添加 readResolve() 方法.
ObjectInputStream 读取对象后会先创建一个实例, 如果实例中存在readResolve()方法,
则会调用该方法,如返回的实例与之前序列化创建的实例不是一个,则会用新的实例替换掉.
```java
public class SerializeSingleton implements Serializable {
    private SerializeSingleton() {}
    private static final SerializeSingleton INSTANCE = new SerializeSingleton();
    public static SerializeSingleton getInstance() {
        return INSTANCE;
    }
    public Object readResolve() {
        return INSTANCE;
    }
}
```
另外, 枚举类在JDK层面就已经做好了防止被破坏的处理.

```java
public class DestroySingletonByReflect {
    public static void main(String[] args){
      EnumSingleton instance = EnumSingleton.getInstance();
          instance.setData(new Object());
      
          Class<EnumSingleton> clazz = EnumSingleton.class;
          // 通过JAD反编译后查看, 发现不存在无参构造
          // EnumSingleton object = (EnumSingleton)getObject(clazz);
      
          // 采用有参构造来创建
          Constructor<EnumSingleton> c = clazz.getDeclaredConstructor(String.class, int.class);
          c.setAccessible(true);
          EnumSingleton object = c.newInstance("Test", 123);
          
          // 报错如下
          // java.lang.IllegalArgumentException: Cannot reflectively create enum objects
          // 	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)
      
          // JDK在底层已经处理好了枚举的安全问题: c.newInstance进去, 源码如下
          // if ((clazz.getModifiers() & Modifier.ENUM) != 0)
          //    throw new IllegalArgumentException("Cannot reflectively create enum objects");
      
          System.out.println(instance == object);
          System.out.println(instance.getData() == object.getData());
    }
}
```
以及在反序列化时调用的也是枚举本身的方法:
```java
Enum<?> en = Enum.valueOf((Class)cl, name);
```
所以说, 枚举已经有JDK帮我们处理了线程安全和防止反射及反序列化的问题.