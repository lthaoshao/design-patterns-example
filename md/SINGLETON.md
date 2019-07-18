# 设计模式

## 单例模式(Singleton Pattern)

单例模式属于创建型模式, 提供了一种创建对象的最佳方式.
确保一个类只存在一个实例, 私有化构造方法, 由该类负责创建自己的实例, 并对外提供全局获取该实例的方法.

![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/src/main/resources/com.lthaoshao.pattern.singleton/singleton.png "简单工厂模式")

### 1. 饿汉式单例

* 直接初始化

* 静态块初始化


    优点:
        1) 绝对线程安全, 在线程还没出现前就进行了初始化.
        2) 没有加任何锁, 执行效率高;
        3) 在用户体验上比懒汉式要好;
    缺点:
        1) 在类加载的时候就完成了初始化, 无论是否使用, 都占用了空间, 浪费内存.


### 2. 懒汉式单例


### 3. 注册式单例

* 枚举式单例

* 容器式单例

### 4. ThreadLocal线程内单例

## 破坏单例行为

### 1. 通过反射破坏单例


### 2. 通过序列化破坏单例


### 3. 如何避免单例遭到破坏