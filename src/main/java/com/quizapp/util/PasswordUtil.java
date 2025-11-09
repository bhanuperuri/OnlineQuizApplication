package com.quizapp.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

public class PasswordUtil {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS = 65536; // strong enough for demo
    private static final int KEY_LENGTH = 256;   // bits

    // Generate a base64-encoded random salt
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash a password with the provided base64-encoded salt using PBKDF2WithHmacSHA256
    public static String hash(char[] password, String base64Salt) {
        try {
            byte[] salt = Base64.getDecoder().decode(base64Salt);
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] key = skf.generateSecret(spec).getEncoded();
            // wipe spec's internal password copy if possible
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(key);
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing a password", e);
        } finally {
            // best-effort: caller should zero the password array after use
        }
    }

    // Verify password by hashing and comparing with storedBase64Hash
    public static boolean verify(char[] password, String base64Salt, String storedBase64Hash) {
        String computed = hash(password, base64Salt);
        // constant-time comparison
        byte[] a = computed.getBytes();
        byte[] b = storedBase64Hash.getBytes();
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) result |= a[i] ^ b[i];
        // wipe temporary arrays
        Arrays.fill(a, (byte)0);
        Arrays.fill(b, (byte)0);
        return result == 0;
    }
}

