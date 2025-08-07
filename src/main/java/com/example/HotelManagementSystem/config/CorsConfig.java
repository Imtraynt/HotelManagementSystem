package com.example.HotelManagementSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")  // allow all endpoints
                    .allowedOrigins("http://localhost:5173")  // your React app origin
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // allowed HTTP methods
                    .allowedHeaders("*")  // allow all headers
                    .allowCredentials(true);  // allow cookies/auth if needed
        }
    }