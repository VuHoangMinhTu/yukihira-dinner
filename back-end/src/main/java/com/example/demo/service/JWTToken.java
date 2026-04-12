package com.example.demo.service;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JWTToken {
    private final JwtEncoder jwtEncoder;
    public String generateToken(Authentication authentication) {
        Instant instantNow = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(grantedAuthority -> {
                    return grantedAuthority.getAuthority();
                }).collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")   // Người phát hành
                .issuedAt(instantNow)
                .expiresAt(instantNow.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope",scope)
                .build();
              return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }


}
