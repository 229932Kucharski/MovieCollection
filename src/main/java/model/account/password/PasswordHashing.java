package model.account.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHashing {

    private final static int ITERATION_COUNT = 5;

    public static byte[] hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Cant find alogrithm for password hashing");
            return null;
        }
        md.update(password.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }

}
