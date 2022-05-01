package com.pucsp.oauthhelper.domain.services;

public interface PhoneService {
    void sendSMS(String phone, String otp);
}
