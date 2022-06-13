package com.example.demo.king.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * server
 *
 * @author sandykang
 */
@Configuration
public class HeartBeatConfig {
    @Value("${channel.id:1}")
    private long id;


    @Bean(value = "heartBeat")
    public CustomProtocol heartBeat() {
        return new CustomProtocol(id, "ping");
    }
}
