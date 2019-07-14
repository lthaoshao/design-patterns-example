# 设计模式

## 一、. 工厂模式

### 1. 简单工厂模式(Simple Factory Pattern)

是指由一个工厂对象决定创建出哪种产品类的实例, 它不属于23种设计模式之一.

简单工厂适用于工厂类负责创建的对象较少的场景, 且客户端只需要传入工厂类的参数，对于如何创
建对象的逻辑不需要关心.
    
![avatar](com.lthaoshao.pattern.factory/simpleFactory.png "简单工厂模式" )

### 2. 工厂方法模式(Factory Method Pattern)

指定义一个创建对象的接口, 但让实现这个接口的类来决定实例化哪个类, 工厂方法让类的实例化推迟到子类中进行.

工厂方法中, 用户只需要关心所需产品的工厂, 而无需关心产品创建的细节, 且加入新的产品, 符合开闭原则.

 ![avatar](com.lthaoshao.pattern.factory/factoryMehtod.png "工厂方法模式" )

### 3. 抽象工厂模式(Abstract Factory Pattern)
 
指提供一个创建一系列相关或相互依赖对象的接口, 无需指定他们具体的类.

客户端(应用层)不依赖于产品类实例如何被创建和实现等细节, 强调的是一系列相关的产品对象(属于同一产品族)一起使用创建对象需要大量重复的代码. 需要提供一个产品类的库, 所有的产品以同样的接口出现, 从而使客户端不依赖于具体实现.
       
 ![avatar](com.lthaoshao.pattern.factory/abstractFactory.png "抽象工厂模式" )
