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
