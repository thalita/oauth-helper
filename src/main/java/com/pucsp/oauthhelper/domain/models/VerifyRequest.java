package com.pucsp.oauthhelper.domain.models;

import lombok.Data;

@Data
public class VerifyRequest {
    String identifier;
    String code;
}
