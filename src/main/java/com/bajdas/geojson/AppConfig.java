package com.bajdas.geojson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
    public class AppConfig {
        @Bean
        public RestTemplate restTemplate() {
            RestTemplate template = new RestTemplate();
            MappingJackson2HttpMessageConverter mapper =
                    new MappingJackson2HttpMessageConverter();
            mapper.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON_UTF8,MediaType.TEXT_PLAIN, MediaType.TEXT_HTML));
            template.getMessageConverters().add(mapper);
            return template;
        }
    }

