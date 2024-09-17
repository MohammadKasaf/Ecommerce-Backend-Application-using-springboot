package com.ecommerce;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretKeyMakerTest {

    @Test
    public void generateSecretKey(){

        //generating a secret key
        SecretKey key= Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

        //Converting the secret key to a hexadecimal string
        String encodedKey= DatatypeConverter.printHexBinary(key.getEncoded());

        //print the encoded secret key to the console
        System.out.println("Encoded Secret Key: "+encodedKey);
    }
}
