package com.assets.options.main;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ObjectMapperProvider {

    private static ObjectMapper OBJECT_MAPPER;
    private static RestTemplate REST_TEMPLATE;

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
            REST_TEMPLATE = new RestTemplate();

            final MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
            jacksonConverter.setObjectMapper(objectMapper());
            REST_TEMPLATE.setMessageConverters(Arrays.asList(jacksonConverter));
        }
        return REST_TEMPLATE;
    }
}
