package com.bajdas.geojson.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        MappingJackson2HttpMessageConverter mapper =
                new MappingJackson2HttpMessageConverter();
        mapper.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8,
                MediaType.TEXT_PLAIN, MediaType.TEXT_HTML));
        template.getMessageConverters().add(mapper);
        return template;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(4, threadFactory());
    }

    private ThreadFactory threadFactory() {
        return new ThreadFactory() {
            int counter;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"obliczacz " + ++counter);
            }
        };
    }
}

