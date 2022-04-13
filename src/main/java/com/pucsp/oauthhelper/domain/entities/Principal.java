package com.pucsp.oauthhelper.domain.entities;

import com.pucsp.oauthhelper.domain.types.Chanell;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Principal")
@Data
@Builder
public class Principal implements Serializable {
    @Id
    private String identifier;
    private String email;
    private String phone;
    private Chanell channel;
    private String secretKey;
}
