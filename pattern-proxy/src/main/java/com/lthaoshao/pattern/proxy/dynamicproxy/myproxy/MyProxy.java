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
        System.out.println(src);

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
