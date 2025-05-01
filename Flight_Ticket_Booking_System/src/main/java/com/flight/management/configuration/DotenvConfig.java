package com.flight.management.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class DotenvConfig implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// Only load dotenv if a specific environment variable is NOT set (e.g.,
		// production indicator)
		if (System.getenv("RENDER") == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

			Properties properties = new Properties();
			dotenv.entries().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

			PropertiesPropertySource propertySource = new PropertiesPropertySource("dotenv", properties);
			environment.getPropertySources().addFirst(propertySource);
		}
	}
}
