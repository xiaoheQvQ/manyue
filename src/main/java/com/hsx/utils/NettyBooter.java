package com.hsx.utils;

import com.hsx.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/** Netty启动器：当springboot容器启动时，启动Netty服务器
 * Created by Mingway on 2019/5/2.
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 监听ApplicationContext刷新的事件
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 此时还未启动容器上下文信息，先启动netty服务器
        if (event.getApplicationContext().getParent() == null) {
            try {
                WSServer.getInstance().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}