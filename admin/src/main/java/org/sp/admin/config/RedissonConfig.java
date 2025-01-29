package org.sp.admin.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ObjectInputFilter;

/**
 * Date:2025/01/29 20:37:39
 * Author：Tobin
 * Description:
 */
@Component
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String address;
    @Value("${spring.data.redis.port}")
    private String port;

    @Bean
    public Redisson redisson() {

        Config config = new Config();

        config.useSingleServer().setAddress(String.format("redis://%s:%s", address, port));

        return (Redisson) Redisson.create(config);
    }

}