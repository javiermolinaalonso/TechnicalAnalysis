package com.assets.options.main;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ObjectMapperProvider {

    private static ObjectMapper OBJECT_MAPPER;
    private static RestTemplate REST_TEMPLATE;
    private static RetryTemplate RETRY_TEMPLATE;

    public static ObjectMapper objectMapper() {
        if (OBJECT_MAPPER == null) {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
        }
        return OBJECT_MAPPER;
    }

    public static RestTemplate restTemplate() {
        if (REST_TEMPLATE == null) {
            SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(5000);
            clientHttpRequestFactory.setReadTimeout(5000);
            REST_TEMPLATE = new RestTemplate(clientHttpRequestFactory);

            final MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
            jacksonConverter.setObjectMapper(objectMapper());
            REST_TEMPLATE.setMessageConverters(Arrays.asList(jacksonConverter));
        }
        return REST_TEMPLATE;
    }

    public static RetryTemplate retryTemplate() {
        if (RETRY_TEMPLATE == null) {
            SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
            retryPolicy.setMaxAttempts(3);

            FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
            backOffPolicy.setBackOffPeriod(5); // 1.5 seconds

            RETRY_TEMPLATE = new RetryTemplate();
            RETRY_TEMPLATE.setRetryPolicy(retryPolicy);
            RETRY_TEMPLATE.setBackOffPolicy(backOffPolicy);
        }

        return RETRY_TEMPLATE;
    }
}
