package com.example.demo.config.web;

import com.example.demo.utils.json.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JsonConfig {

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = JsonUtils.OBJECT_MAPPER;
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
