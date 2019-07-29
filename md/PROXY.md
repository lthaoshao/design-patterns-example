# [设计模式](../README.md)

## 代理模式(Proxy Pattern)

### 概念

代理模式，指为其他对象提供一种代理，以控制该对象的访问。代理对象在客户端和目标对象之间起到中介的作用，属于结构型设计模式。

### 目的

一是保护对象，二是增强对象。

### 适用场景

在我们的生活中有一些场景，如黄牛买票、婚介、经纪人、租房中介、快递、事务代理等，这些都是代理模式的具体体现。例如，在访问敏感对象时，应该可以检查客户端是否具有所需的访问权限。

### UML
![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/src/main/resources/com.lthaoshao.pattern.prototype/proxy.png "代理模式")

### 静态代理

### DBRoute初版

### 动态代理
#### JDK动态代理

#### cglib动态代理

#### 自定义动态代理


书写代理类, 编译成字节码文件, 并加载到内存中;

### DBRoute改版
### 总结

保存代理类字节码文件, 并进行反编译查看内容;


