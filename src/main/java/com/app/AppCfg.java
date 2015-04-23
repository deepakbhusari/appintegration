package com.app;

import java.io.IOException;

import javax.inject.Singleton;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.app.data.DataStoreImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Singleton
@Configuration
@ImportResource({ "classpath:com/app/applicationContext.xml" })
public class AppCfg {
	private final Logger l = LoggerFactory.getLogger(AppCfg.class);
	private DataStoreImpl dataStoreImpl;
	private ObjectMapper mapper = new ObjectMapper();

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/");
		resolver.setSuffix(".jsp");
		l.info("application config");
		return resolver;
	}

	@Bean(name = "dataStoreImpl")
	public DataStoreImpl getDatastore() {
		if (dataStoreImpl == null) {
			dataStoreImpl = new DataStoreImpl();
		}
		return dataStoreImpl;
	}

	public ObjectMapper getObjectMapper() {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper;
	}

	public HttpResponse eventData(String url) throws JAXBException, IOException {
		final HttpClient client = HttpClients.createDefault();
		final HttpGet httpRequest = new HttpGet(url);

		HttpResponse resp = client.execute(httpRequest);
		if (resp.getStatusLine().getStatusCode() == 200) {
			return resp;
		}

		return null;
	}
}
