package com.pucsp.oauthhelper.domain;

import lombok.Data;
import lombok.Getter;

@Getter
public class CreateResponse {

    public CreateResponse(String code) {
        this.code = code;
    }
    String code;
}
