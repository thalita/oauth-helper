package com.pucsp.oauthhelper.domain.entities.builders;

import com.pucsp.oauthhelper.domain.entities.Principal;

public class PrincipalBuilder {

     public static Principal build(String identifier, String code){
         return Principal.builder()
                 .identifier(identifier)
                 .code(code)
                 .build();
     }
}
