package com.cypher.aes.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Generador de claves AES-256 con IV aleatorio.
 * Cada operación produce una clave y un IV únicos.
 */
public class AesCipher {

    private static final int KEY_SIZE = 256;
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private final SecureRandom secureRandom;

    public AesCipher() {
        this.secureRandom = new SecureRandom();
    }

    /**
     * Genera una clave AES-256 aleatoria y su IV.
     * @return un array con [clave en Base64, IV en Base64]
     */
    public String[] generateKeyAndIV() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(KEY_SIZE, secureRandom);
            SecretKey secretKey = keyGen.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            String keyBase64 = Base64.getEncoder().encodeToString(keyBytes);
            String ivBase64 = Base64.getEncoder().encodeToString(iv);

            return new String[]{keyBase64, ivBase64};
        } catch (Exception e) {
            throw new RuntimeException("Error generando clave AES-256", e);
        }
    }

    /**
     * Convierte una clave en Base64 a SecretKey.
     */
    public SecretKey stringToSecretKey(String keyBase64) {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Convierte un IV en Base64 a bytes.
     */
    public byte[] stringToIV(String ivBase64) {
        return Base64.getDecoder().decode(ivBase64);
    }

    /**
     * Cifra texto usando AES-256-GCM.
     */
    public String encrypt(String plaintext, SecretKey secretKey, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintextBytes);

            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando con AES-256-GCM", e);
        }
    }

    /**
     * Descifra texto usando AES-256-GCM.
     */
    public String decrypt(String ciphertextBase64, SecretKey secretKey, byte[] iv) {
        try {
            byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertextBase64);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando con AES-256-GCM: clave o texto inválido", e);
        }
    }
}
