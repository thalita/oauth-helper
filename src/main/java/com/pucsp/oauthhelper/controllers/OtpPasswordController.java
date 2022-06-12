package com.pucsp.oauthhelper.controllers;


import com.pucsp.oauthhelper.domain.entities.Principal;
import com.pucsp.oauthhelper.domain.models.CreateRequest;
import com.pucsp.oauthhelper.domain.models.CreateResponse;
import com.pucsp.oauthhelper.domain.models.VerifyRequest;
import com.pucsp.oauthhelper.domain.services.EmailService;
import com.pucsp.oauthhelper.domain.services.PhoneService;
import com.pucsp.oauthhelper.domain.services.PrincipalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pucsp.oauthhelper.config.CustomConfig.generateSecretKey;
import static com.pucsp.oauthhelper.config.CustomConfig.generateTOTP;
import static com.pucsp.oauthhelper.domain.entities.builders.PrincipalBuilder.build;

@RestController
@RequestMapping("/otp")
public class OtpPasswordController {

    private final PrincipalRepository repository;
    private final EmailService emailService;
    private final PhoneService phoneService;

    public OtpPasswordController(PrincipalRepository repository, EmailService emailService, PhoneService phoneService) {
        this.repository = repository;
        this.emailService = emailService;
        this.phoneService = phoneService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateRequest createRequest) {
        var principalCache = repository.findById(createRequest.getIdentifier());
        principalCache.ifPresent(repository::delete);

        var secretKey = generateSecretKey();
        var otp = generateTOTP(secretKey);

        Principal principal = build(createRequest.getIdentifier(), otp);
        repository.save(principal);


        switch (createRequest.getChannel()) {
            case "email":
                emailService.sendEmail(createRequest.getEmail().orElseThrow(), otp);
                break;
             case "phone":
                 phoneService.sendSMS(createRequest.getPhone().orElseThrow(), otp);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequest verifyRequest) {

        var principal = repository.findById(verifyRequest.getIdentifier());
        if (principal.isPresent()) {

            var otp = principal.get().getCode();

            if (verifyRequest.getCode().equals(otp)) {
                repository.delete(principal.get());

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
