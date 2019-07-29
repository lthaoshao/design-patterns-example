package com.lthaoshao.pattern.proxy.simple;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/7/29 23:11
 */
public class Proxy implements Subject{

    private Subject subject;
    public Proxy(Subject subject) {
        this.subject = subject;
    }


    @Override
    public void operation() {
        doBefore();
        subject.operation();
        doAfter();
    }

    private void doAfter() {
        System.out.println("操作后执行。。。");
    }

    private void doBefore() {
        System.out.println("操作前执行。。。");
    }
}
