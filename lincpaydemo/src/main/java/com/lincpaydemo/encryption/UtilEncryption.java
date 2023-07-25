package com.lincpaydemo.encryption;

import android.os.Build;
import android.util.Log;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UtilEncryption {


    public static String getPayLoad(String json, String encryptedKey) {

        return getEncryptedString(json,encryptedKey);
    }

    private static String getEncryptedString(String parameterJson, String encryptKey) {


        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encryptedData = cipher.doFinal(parameterJson.getBytes("UTF-8"));

            byte[] combinedBytes = new byte[ivBytes.length + encryptedData.length];
            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedData, 0, combinedBytes, ivBytes.length, encryptedData.length);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(combinedBytes);
            }
        } catch (Exception e) {
            //LOGGER.error("Exception in encrypt() : encryptMData- " + encryptKey + " : Message- " + e.getMessage());
            Log.e("Flow","Exception in encrypt() ");
        }
        return null;
    }
    private static Key setKey(String encryptKey) {

        MessageDigest sha = null;
        try {
            byte[] key = encryptKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            Key secretKey = new SecretKeySpec(key, "AES");
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
