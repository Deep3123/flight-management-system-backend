package com.flight.management.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final EncryptedRequestConverter encryptedRequestConverter;

	public WebMvcConfig(EncryptedRequestConverter encryptedRequestConverter) {
		this.encryptedRequestConverter = encryptedRequestConverter;
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, encryptedRequestConverter); // Register with highest priority
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.TEXT_PLAIN).favorParameter(false).ignoreAcceptHeader(false)
				.mediaType("json", MediaType.APPLICATION_JSON).mediaType("plain", MediaType.TEXT_PLAIN);
	}
}
