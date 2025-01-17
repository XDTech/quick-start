package org.sp.admin.config;

import jakarta.annotation.Resource;
import org.sp.admin.component.RedisRemoveListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Date:2024/11/20 13:35:11
 * Author：Tobin
 * Description: redis 监听bean
 */
@Configuration
public class RedisListenerConfig {


    @Resource
    private RedisRemoveListener redisDeleteListener;
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //监听所有key的删除事件
        container.addMessageListener(redisDeleteListener,redisDeleteListener.getTopic());
        return container;
    }

}
