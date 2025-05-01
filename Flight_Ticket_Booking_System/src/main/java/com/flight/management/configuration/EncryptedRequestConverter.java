//package com.flight.management.configuration;
//
//import com.flight.management.util.EncryptionUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.HttpOutputMessage;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.AbstractHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.http.converter.HttpMessageNotWritableException;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class EncryptedRequestConverter extends AbstractHttpMessageConverter<Object> {
//
//	private final EncryptionUtil encryptionUtil;
//	private final ObjectMapper objectMapper;
//
//	public EncryptedRequestConverter(EncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
//		super(MediaType.APPLICATION_JSON);
//		this.encryptionUtil = encryptionUtil;
//		this.objectMapper = objectMapper;
//	}
//
//	@Override
//	protected boolean supports(Class<?> clazz) {
//		// Supports all classes – adjust if you want fine control
//		return true;
//	}
//
//	@Override
//	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
//			throws IOException, HttpMessageNotReadableException {
//		// Read encrypted string
//		String encryptedData = new String(inputMessage.getBody().readAllBytes());
//		System.err.println("encryptedData-----> " + encryptedData);
//
//		// Decrypt
//		String decryptedJson = encryptionUtil.decrypt(encryptedData);
//		System.err.println("decryptedJson-----> " + decryptedJson);
//		// Deserialize decrypted JSON to object
//		return objectMapper.readValue(decryptedJson, clazz);
//	}
//
//	@Override
//	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
//			throws IOException, HttpMessageNotWritableException {
//		// Serialize object to JSON
//		String jsonData = objectMapper.writeValueAsString(object);
//		System.err.println("jsonData-----> " + jsonData);
//
//		// Encrypt
//		String encryptedData = encryptionUtil.encrypt(jsonData);
//		System.err.println("encryptedData-----> " + encryptedData);
//
//		// Write encrypted data to response
//		outputMessage.getBody().write(encryptedData.getBytes());
//	}
//}

package com.flight.management.configuration;

import com.flight.management.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;

@Component
public class EncryptedRequestConverter extends AbstractHttpMessageConverter<Object> {

	private final EncryptionUtil encryptionUtil;
	private final ObjectMapper objectMapper;

	public EncryptedRequestConverter(EncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
		// Support both application/json and text/plain for encrypted content
		super(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
		this.encryptionUtil = encryptionUtil;
		this.objectMapper = objectMapper;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// Supports all classes – adjust if you want fine control
		return true;
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		// Read encrypted string
		String encryptedData = new String(inputMessage.getBody().readAllBytes());
		System.err.println("encryptedData-----> " + encryptedData);

		// Decrypt
		String decryptedJson = encryptionUtil.decrypt(encryptedData);
		System.err.println("decryptedJson-----> " + decryptedJson);
		// Deserialize decrypted JSON to object
		return objectMapper.readValue(decryptedJson, clazz);
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// Serialize object to JSON
		String jsonData = objectMapper.writeValueAsString(object);
		System.err.println("jsonData-----> " + jsonData);

		// Encrypt
		String encryptedData = encryptionUtil.encrypt(jsonData);
		System.err.println("encryptedData-----> " + encryptedData);

		// Set content type to text/plain explicitly for responses
		outputMessage.getHeaders().setContentType(MediaType.TEXT_PLAIN);

		// Write encrypted data to response
		outputMessage.getBody().write(encryptedData.getBytes());
	}
}