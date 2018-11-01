package com.lyz.lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by lyz on 2018/9/3.
 *
 */

public class EnciphermentUtil {

    // java.security  : 加密


    public SecureRandom getRandom() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
//        secureRandom.setSeed(145);
//        secureRandom.setSeed(bytes);
        secureRandom.nextBytes(bytes);
        return secureRandom;
    }

    // change code to byte array and enciphering it to base64 for result.
    public String getBase64String(byte[] input) {
//        try {
//            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
//            sha256.update(input);
//            return android.util.Base64.encodeToString(sha256.digest(), android.util.Base64.DEFAULT);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public String getMD5String(byte[] input) {
//        try {
//            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
//            sha256.update(input);
//            return android.util.Base64.encodeToString(sha256.digest(), android.util.Base64.DEFAULT);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}

