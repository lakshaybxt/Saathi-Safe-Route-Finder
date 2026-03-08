package com.saathi.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtils {

    private final String ALGO = "AES";
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${encryption.secret.key}")
    private String secretKey;

    /**
     * Encrypts the given data using AES encryption.
     *
     * @param data the plaintext data to encrypt
     * @return the encrypted data as a Base64 encoded string
     * @throws Exception if encryption fails
     */
    private String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGO);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    /**
     * Decrypts the given encrypted data using AES decryption.
     *
     * @param encryptedData the encrypted data as a Base64 encoded string
     * @return the decrypted plaintext data
     * @throws Exception if decryption fails
     */
    private String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGO);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decoded));
    }

    /**
     * Encrypts an object by converting it to JSON and then encrypting the JSON string.
     *
     * @param obj the object to encrypt
     * @return the encrypted data as a Base64 encoded string
     * @throws Exception if encryption fails
     */
    public String encryptJson(Object obj) throws Exception {
        String json = mapper.writeValueAsString(obj);
        return encrypt(json);
    }

    /**
     * Decrypts the given encrypted data and converts the resulting JSON string back to an object of the specified type.
     *
     * @param encryptedData the encrypted data as a Base64 encoded string
     * @param valueType     the class of the object to return
     * @param <T>           the type of the object to return
     * @return the decrypted object
     * @throws Exception if decryption or JSON parsing fails
     */
    public <T> T decryptJson(String encryptedData, Class<T> valueType) throws Exception {
        String json = decrypt(encryptedData);
        return mapper.readValue(json, valueType);
    }

}
