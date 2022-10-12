package com.example.demo.config.es;

import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EsConfig {


    @Bean
    @Primary
    @ConfigurationProperties(
            prefix = "spring.elasticsearch.rest.myself"
    )
    public ElasticsearchRestClientProperties elasticsearchRestClientProperties() {
        return new ElasticsearchRestClientProperties();
    }
}
