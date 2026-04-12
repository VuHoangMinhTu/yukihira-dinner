package com.example.demo.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public record RSAKeyLoader(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}
