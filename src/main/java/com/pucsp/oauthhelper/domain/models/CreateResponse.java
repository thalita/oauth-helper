package com.pucsp.oauthhelper.domain.models;

import lombok.Getter;

@Getter
public class CreateResponse {

    public CreateResponse(String code) {
        this.code = code;
    }
    String code;
}
