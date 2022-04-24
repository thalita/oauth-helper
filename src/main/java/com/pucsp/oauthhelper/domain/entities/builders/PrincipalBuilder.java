package com.pucsp.oauthhelper.domain.entities.builders;

import com.pucsp.oauthhelper.domain.entities.Principal;
import com.pucsp.oauthhelper.domain.models.CreateRequest;
import com.pucsp.oauthhelper.domain.types.Chanell;

import static com.pucsp.oauthhelper.config.CustomConfig.generateSecretKey;

public class PrincipalBuilder {

     public static Principal build(CreateRequest createRequest){
         return Principal.builder()
                 .identifier(createRequest.getIdentifier())
                 .secretKey(generateSecretKey())
                 .channel(Enum.valueOf(Chanell .class, createRequest.getChanell().toUpperCase()))
                 .email(createRequest.getEmail())
                 .phone(createRequest.getPhone())
                 .build();
     }
}
