package com.example.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.api-key}")
    public String API_KEY;
    @Value("${cloudinary.api-secret}")
    public String API_SECRET;
    @Value("${cloudinary.name}")
    public String CLOUD_NAME;

    @Bean
    public Cloudinary createCloudinary() {
        Cloudinary cloudinary = new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", API_KEY,
                        "api_secret", API_SECRET
                ));
        return cloudinary;
    }
}
