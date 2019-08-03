package com.lthaoshao.pattern.delegate;
/**
 * <p> 老板，发任务 </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 14:01
 */
public class Boss {

    public void command(String command, Leader leader){
        leader.doing(command);
    }
}
