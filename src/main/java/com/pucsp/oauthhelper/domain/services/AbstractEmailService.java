package com.pucsp.oauthhelper.domain.services;

import com.pucsp.oauthhelper.domain.models.CreateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    private final MailSender mailSender;

    public AbstractEmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendEmail(CreateRequest request, String otp) {
        var sm = prepareSimpleMailMesssage(request, otp);
        mailSender.send(sm);
    }

    private SimpleMailMessage prepareSimpleMailMesssage(CreateRequest request, String otp) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(request.getEmail());
        sm.setFrom(sender);
        sm.setSubject("OTP message");
        sm.setSentDate((new Date(System.currentTimeMillis())));
        sm.setText(otp);
        return sm;

    }
}
