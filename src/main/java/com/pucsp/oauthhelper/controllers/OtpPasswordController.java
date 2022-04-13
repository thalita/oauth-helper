package com.pucsp.oauthhelper.controllers;


import com.pucsp.oauthhelper.domain.entities.Principal;
import com.pucsp.oauthhelper.domain.models.CreateRequest;
import com.pucsp.oauthhelper.domain.models.CreateResponse;
import com.pucsp.oauthhelper.domain.models.VerifyRequest;
import com.pucsp.oauthhelper.domain.services.EmailService;
import com.pucsp.oauthhelper.domain.services.PrincipalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.pucsp.oauthhelper.config.CustomConfig.generateTOTP;
import static com.pucsp.oauthhelper.domain.entities.builders.PrincipalBuilder.build;

@RestController
public class OtpPasswordController {

    private final PrincipalRepository repository;
    private final EmailService emailService;

    public OtpPasswordController(PrincipalRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @PostMapping("/security/otp/create")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateRequest createRequest) {

        var principalCache = repository.findById(createRequest.getIdentifier());
        if (principalCache.isPresent()) {
            repository.delete(principalCache.get());
        }
        Principal principal = build(createRequest);
        repository.save(principal);

        var otp = generateTOTP(principal.getSecretKey());

        // TODO: remover para produção
        System.out.println("secretKey: " + principal.getSecretKey());
        System.out.println("OTP: " + otp);

        // TODO: aqui deve enviar o codigo otp conforme channel do request
        switch (principal.getChannel()) {
            case EMAIL:
                emailService.sendEmail(createRequest, otp);
            case PHONE:
                break;
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/security/otp/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequest verifyRequest) {

        var principal = repository.findById(verifyRequest.getIdentifier());
        if (principal.isPresent()) {

            var otp = generateTOTP(principal.get().getSecretKey());
            repository.delete(principal.get());

            if (verifyRequest.getCode().equals(otp)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



}
