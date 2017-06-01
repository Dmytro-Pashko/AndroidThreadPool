package com.goodvin1709.corgigallery.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    private static final String ALGORITM = "MD5";

    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(ALGORITM);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hex = new StringBuilder();
            for (Byte c : messageDigest) {
                hex.append(Integer.toHexString(0xFF & c));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.log("Algoritm %s for hashing cached files not found.", ALGORITM);
            return "";
        }
    }
}
