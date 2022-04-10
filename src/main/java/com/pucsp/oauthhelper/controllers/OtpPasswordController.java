package com.pucsp.oauthhelper.controllers;


import com.pucsp.oauthhelper.domain.types.Chanell;
import com.pucsp.oauthhelper.domain.models.CreateRequest;
import com.pucsp.oauthhelper.domain.models.CreateResponse;
import com.pucsp.oauthhelper.domain.models.VerifyRequest;
import com.pucsp.oauthhelper.domain.entities.Principal;
import com.pucsp.oauthhelper.domain.services.PrincipalRepository;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.SecureRandom;

@RestController
public class OtpPasswordController {

    private final PrincipalRepository repository;

    public OtpPasswordController(PrincipalRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/security/otp/create")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateRequest createRequest){

        var principalCache = repository.findById(createRequest.getIdentifier());
        if (principalCache.isPresent()){
            repository.delete(principalCache.get());
        }

        var principal = new Principal();

        principal.setIdentifier(createRequest.getIdentifier());
        principal.setSecretKey(generateSecretKey());
        principal.setChannel(Enum.valueOf(Chanell.class, createRequest.getChanell().toUpperCase()));
        principal.setEmail(createRequest.getEmail());
        principal.setPhone(createRequest.getPhone());

        System.out.println("secretKey: "+principal.getSecretKey());

        repository.save(principal);

        var otp = generateTOTP(principal.getSecretKey());

       // TODO: aqui deve enviar o codigo otp conforme channel do request
        switch (principal.getChannel()){
            case EMAIL:
                break;
            case PHONE:
                break;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateResponse(otp));
    }

    @PostMapping("/security/otp/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequest verifyRequest)  {

        var principal  = repository.findById(verifyRequest.getIdentifier());
        if (principal.isPresent()){

            var otp = generateTOTP(principal.get().getSecretKey());
            repository.delete(principal.get());

            if(verifyRequest.getCode().equals(otp)){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        var bytes = new byte[20];
        random.nextBytes(bytes);
        var base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String generateTOTP(String secretKey) {
        var base32 = new Base32();
        var bytes = base32.decode(secretKey);
        var hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

}
