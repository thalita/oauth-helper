package com.pucsp.oauthhelper.domain.models;

import lombok.Data;

@Data
public class CreateRequest {
    private final String identifier;
    private final String channel;
    private final String email;
    private final String phone;
}
