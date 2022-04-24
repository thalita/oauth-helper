package com.pucsp.oauthhelper.domain.services;

import com.pucsp.oauthhelper.domain.models.CreateRequest;
import org.springframework.stereotype.Service;


public interface EmailService {

    void sendEmail(CreateRequest request, String otp);

}
