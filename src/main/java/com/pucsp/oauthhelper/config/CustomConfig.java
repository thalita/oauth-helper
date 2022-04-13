package com.pucsp.oauthhelper.config;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.SecureRandom;

@Configuration
public class CustomConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
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
