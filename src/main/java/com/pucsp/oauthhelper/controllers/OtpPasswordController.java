package com.pucsp.oauthhelper.controllers;


import com.pucsp.oauthhelper.domain.CreateRequest;
import com.pucsp.oauthhelper.domain.CreateResponse;
import com.pucsp.oauthhelper.domain.VerifyRequest;
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

    final String secret = "BEBRK7OOBMFIO6VLQEFSL2DRU6ETTN5J";

    @PostMapping("/security/otp/create")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateRequest createRequest){

        var key = generateSecretKey();

        // persiste secret em cache
       //  21865528846:BEBRK7OOBMFIO6VLQEFSL2DRU6ETTN5J
        var otp = generateTOTP(secret);

        // disparo sms ou email

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateResponse(otp));

    }

    @PostMapping("/security/otp/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequest verifyRequest)  {

        // recuperar secret do cache

        var otp = generateTOTP(secret);

       if(verifyRequest.getCode().equals(otp)){
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
