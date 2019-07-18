package com.lthaoshao.pattern.singleton.register;

/**
 * <p> 枚举式单例 </p>
 * 通过JAD 工具进行反编译后的文件,见  resources/com.lthaoshao.pattern.singleton/EnumSingleton.jad
 * 工具下载:https://varaneckas.com/jad/
 * 下载完的JAD执行文件可以放到 环境变量 JAVA_HOME 的bin目录中, 方便执行.
 *
 * @author lijinghao
 * @version : EnumSingleton.java, v 0.1 2019年07月18日 14:12:12 lijinghao Exp $
 */
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


