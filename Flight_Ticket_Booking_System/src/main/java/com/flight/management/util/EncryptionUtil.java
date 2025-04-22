package com.flight.management.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {
	private static final String SECRET_KEY = "x2B7eTf93mQ9cGzYdFk7pLm8XsRjHtNv"; // 32-byte key for AES-256
	private static final String INIT_VECTOR = "7fH1d9Lm3cQ5x7Vz"; // 16-byte IV
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	public String encrypt(String data) {
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			throw new RuntimeException("Encryption error", ex);
		}
	}

	public String decrypt(String encryptedData) {
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
			return new String(original, "UTF-8");
		} catch (Exception ex) {
			throw new RuntimeException("Decryption error", ex);
		}
	}
}
