package com.pucsp.oauthhelper.domain.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void deveEnviarEmailQuandoAcionado() {
        emailService.sendEmail("aaa", "bbb");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}