package com.pucsp.oauthhelper.config;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomConfig {


    public static final String INDEX_IDENTIFIER = "indexIdentifier";
    private static final Integer MINUTES = 2;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        Map<String, String> env = System.getenv();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
            env.getOrDefault("REDIS_HOST", "localhost"),
            6379
        );
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(){
        return (builder) -> {
            Map<String, RedisCacheConfiguration> mapConfig = new HashMap<>();
            mapConfig.put(INDEX_IDENTIFIER, RedisCacheConfiguration.defaultCacheConfig())
                    .entryTtl(Duration.ofMinutes(MINUTES));
            builder.withInitialCacheConfigurations(mapConfig);
        };
    }

    @Bean
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        var bytes = new byte[20];
        random.nextBytes(bytes);
        var base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    @Bean
    public static String generateTOTP(String secretKey) {
        var base32 = new Base32();
        var bytes = base32.decode(secretKey);
        var hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
}
