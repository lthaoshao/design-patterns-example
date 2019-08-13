# [设计模式](../README.md)

## 工厂模式(Factory Pattern)

### 1. 简单工厂模式(Simple Factory Pattern)

是指由一个工厂对象决定创建出哪种产品类的实例, 它不属于23种设计模式之一.

简单工厂适用于工厂类负责创建的对象较少的场景, 且客户端只需要传入工厂类的参数，对于如何创
建对象的逻辑不需要关心.

    优点:
        1) 屏蔽了产品的具体实现, 调用者只关心工厂接口;
        2) 实现简单;
    缺点:
        1) 增加产品, 需要修改工厂类, 不符合 "开放-关闭原则";
        2) 工厂中集成了所有的实例的创建逻辑, 高内聚责任分配原则    
![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-factory/src/main/resources/simpleFactory.png?raw=true "简单工厂模式")
### 2. 工厂方法模式(Factory Method Pattern)

指定义一个创建对象的接口, 但让实现这个接口的类来决定实例化哪个类, 工厂方法让类的实例化推迟到子类中进行.

工厂方法中, 用户只需要关心所需产品的工厂, 而无需关心产品创建的细节, 且加入新的产品, 符合开闭原则.

    优点:
        1) 继承了简单工厂模式的有点;
        2) 符合 "开放-关闭原则";
    缺点:
        1) 增加产品, 需要增加新的工厂类, 导致系统中类的个数成对增加, 在一定程度横渡上增加了系统的复杂性;
![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-factory/src/main/resources/factoryMehtod.png?raw=true "工厂方法模式")


### 3. 抽象工厂模式(Abstract Factory Pattern)
 
指提供一个创建一系列相关或相互依赖对象的接口, 无需指定他们具体的类.

客户端(应用层)不依赖于产品类实例如何被创建和实现等细节, 强调的是一系列相关的产品对象(属于同一产品族)一起使用创建对象需要大量重复的代码. 需要提供一个产品类的库, 所有的产品以同样的接口出现, 从而使客户端不依赖于具体实现.

    优点:
        1) 隔离了具体类的生成, 是客户端并不需要知道什么被创建, 且每次通过具体工厂, 就可以创建产品族中的多个对象;
        2) 增加新的产品族和具体工厂都很方便;
    缺点: 
        1) 增加新的产品等级结构很复杂, 需要修改抽象类和所有的具体工厂, 对"开放-关闭原则"的支持呈现倾斜性;        
![Alt text](https://github.com/lthaoshao/design-patterns-example/blob/master/pattern-factory/src/main/resources/abstractFactory.png?raw=true "抽象工厂模式")

