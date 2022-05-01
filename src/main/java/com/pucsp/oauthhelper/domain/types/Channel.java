package com.pucsp.oauthhelper.domain.types;

public enum Channel {
    EMAIL("email"),
    PHONE("phone");

    private final String name;

    Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
