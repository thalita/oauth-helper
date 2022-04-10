package com.pucsp.oauthhelper.domain.types;

public enum Chanell {
    EMAIL("email"),
    PHONE("phone");

    private final String name;

    Chanell(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
