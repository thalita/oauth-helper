package com.pucsp.oauthhelper.domain;

import lombok.Data;

@Data
public class CreateRequest {

    String identifier;
    Chanell chanell;
    String email;
    String phone;
}
