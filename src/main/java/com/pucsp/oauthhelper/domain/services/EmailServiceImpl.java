package com.pucsp.oauthhelper.domain.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${default.sender}")
    private String sender;

    private final MailSender mailSender;

    public EmailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendEmail(String recipient, String otp) {
        var sm = prepareSimpleMailMesssage(recipient, otp);
        mailSender.send(sm);
    }

    private SimpleMailMessage prepareSimpleMailMesssage(String recipient, String otp) {
        var body = String.format("Olá, Seu código de autenticação: %s", otp);


        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(recipient);
        sm.setFrom(sender);
        sm.setSubject("Código de autenticação");
        sm.setSentDate((new Date(System.currentTimeMillis())));
        sm.setText(body);
        return sm;
    }
}
