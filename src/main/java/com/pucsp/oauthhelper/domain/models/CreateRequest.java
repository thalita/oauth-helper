package com.pucsp.oauthhelper.domain.models;

import lombok.Data;
import java.util.Optional;

@Data
public class CreateRequest {
    private final String identifier;
    private final String channel;
    private final Optional<String> email;
    private final Optional<String> phone;
}
