package com.example.cashdeshmodule.service;

import com.example.cashdeshmodule.service.contracts.HashingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class HashingServiceImpl implements HashingService {

    @Value("${API_KEY}")
    private String storedKey;

    public Boolean verifyApiKey(String apiKey) {

        byte[] combined = Base64.getDecoder().decode(storedKey);
        byte[] salt = new byte[16];
        byte[] storedHashBytes = new byte[combined.length - salt.length];
        System.arraycopy(combined, 0, salt, 0, salt.length);
        System.arraycopy(combined, salt.length, storedHashBytes, 0, storedHashBytes.length);

        KeySpec spec = new PBEKeySpec(apiKey.toCharArray(), salt, 10, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return MessageDigest.isEqual(storedHashBytes, hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }
}
