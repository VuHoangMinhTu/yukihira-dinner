package com.example.demo.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Configuration
public class KeyGeneratorConfig {
    @Value("${rsa.rsa-public-key}")
    private String locationPublicKey;
    @Value("${rsa.rsa-private-key}")
    private String locationPrivateKey;
@Bean
public RSAKeyLoader rsaKeyProperties() throws Exception {
    // 1. Đọc nội dung file .pem
    String publicPem = new String(new FileInputStream(locationPublicKey).readAllBytes());
    String privatePem = new String(new FileInputStream(locationPrivateKey).readAllBytes());

    // 2. Làm sạch chuỗi
    publicPem = publicPem.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

    privatePem = privatePem.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

    // 3. Decode Base64 sang byte[]
    byte[] publicBytes = Base64.getDecoder().decode(publicPem);
    byte[] privateBytes = Base64.getDecoder().decode(privatePem);

    // 4. Tạo Object Key
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKey publicKeyObj = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
    RSAPrivateKey privateKeyObj = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

    // 5. Trả về Record thay vì trả về Map
    return new RSAKeyLoader(publicKeyObj, privateKeyObj);
}
}
