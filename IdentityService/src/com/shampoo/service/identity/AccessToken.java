package com.shampoo.service.identity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AccessToken {

    public String generateAccessToken(String username, String email, String password) throws NoSuchAlgorithmException {
        String temp = username + email + password;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(temp.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder hashText = new StringBuilder(bigInt.toString(16));
        while (hashText.length() < 32) {
            hashText.insert(0, '0');
        }
        return hashText.toString();
    }
}
