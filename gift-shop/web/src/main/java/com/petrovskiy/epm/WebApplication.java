package com.petrovskiy.epm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

/*@EnableEurekaClient*/
@SpringBootApplication
public class WebApplication {

    private static final String ERROR_MESSAGES_FILE = "error_messages";
    private static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


    @Bean
    public ResourceBundleMessageSource getResourceBundleMessageSource() {
        ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
        messages.addBasenames(ERROR_MESSAGES_FILE);
        messages.setDefaultEncoding(ENCODING);
        return messages;
    }
}
