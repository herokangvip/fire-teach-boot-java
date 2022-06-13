package com.example.demo.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * king
 */
@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    /**
     * 本地缓存，一分钟
     *
     * @return CacheManager
     */
    @Bean(name = "minuteLocalCache")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        //Caffeine配置
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                //最后一次写入后经过固定时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                //maximumSize=[long]: 缓存的最大条数
                .maximumSize(1000);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }


    /**
     * 可以定义多个CacheManager,在使用时进行选择
     * 需要指定一个默认的
     *
     * @param redisConnectionFactory factory
     * @return cacheManager
     */
    @Primary
    @Bean("secondRedisCache")
    public CacheManager permissionCacheManager(@Qualifier("lettuceConnectionFactory")
                                                       RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //前缀处理,redisKey
                .computePrefixWith(cacheName -> "secondRedis" + ":" + cacheName + ":")
                //键的序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                //值的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisConfig.redisSerializer()))
                //是否缓存空值
                .disableCachingNullValues()
                //过期时间
                .entryTtl(Duration.ofSeconds(1));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .build();
    }


    /**
     * 分钟redis缓存
     *
     * @param redisConnectionFactory factory
     * @return cacheManager
     */
    @Bean("oneMinRedisCache")
    public CacheManager oneMinRedisCacheManager(@Qualifier("lettuceConnectionFactory")
                                                        RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //前缀处理,redisKey
                .computePrefixWith(cacheName -> "oneMinRedis" + ":" + cacheName + ":")
                //键的序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                //值的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisConfig.redisSerializer()))
                //是否缓存空值
                .disableCachingNullValues()
                //过期时间
                .entryTtl(Duration.ofMinutes(1));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    /**
     * 可以定义多个CacheManager,在使用时进行选择
     * 需要指定一个默认的
     *
     * @param redisConnectionFactory factory
     * @return cacheManager
     */
    @Bean("daysRedisCache")
    public CacheManager daysCacheManager(@Qualifier("lettuceConnectionFactory")
                                                 RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //前缀处理,redisKey
                .computePrefixWith(cacheName -> "daysRedis" + ":" + cacheName + ":")
                //键的序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                //值的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisConfig.redisSerializer()))
                //是否缓存空值
                .disableCachingNullValues()
                //过期时间
                .entryTtl(Duration.ofDays(1));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean("testRedisCache")
    public CacheManager testCacheManager(@Qualifier("lettuceConnectionFactory")
                                                 RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //前缀处理,redisKey
                //键的序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                //值的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisConfig.redisSerializer()))
                //是否缓存空值
                .disableCachingNullValues()
                //过期时间
                .entryTtl(Duration.ofDays(1));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(cacheConfiguration)
                .build();
    }


}


