package com.pucsp.oauthhelper.domain.models;

import lombok.Data;

@Data
public class CreateRequest {

    String identifier;
    String chanell;
    String email;
    String phone;
}
