package com.example.chatapplication.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class cryptoService {
    public cryptoService(){

    }
    public String getSHA256Hash(String input) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
//        // bytes to hex
        StringBuilder output = new StringBuilder();
        for (byte b : hashInBytes) {
            output.append(String.format("%02x", b));
        }
        return output.toString();
    }
}
