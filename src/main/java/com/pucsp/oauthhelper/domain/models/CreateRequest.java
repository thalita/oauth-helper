package com.pucsp.oauthhelper.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {
    private String identifier;
    private String channel;
    private Optional<String> email;
    private Optional<String> phone;
}
