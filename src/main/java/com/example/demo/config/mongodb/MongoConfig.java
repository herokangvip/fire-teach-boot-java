package com.example.demo.config.mongodb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class MongoConfig {

    @Bean
    @Primary
    @ConfigurationProperties(
            prefix = "spring.data.mongodb"
    )
    public MongoProperties mongoProperties() {
        return new TestMongoProperties();
    }

    static class TestMongoProperties extends MongoProperties {

    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.mongodb.transactionEnabled", havingValue = "true")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDbFactory) {
        return new MongoTransactionManager(mongoDbFactory);
    }
}
