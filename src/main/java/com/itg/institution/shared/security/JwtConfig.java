package com.itg.institution.shared.security;

import com.itg.institution.shared.configuration.AppConfig;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Getter
@Setter
@Configuration
@RequiredArgsConstructor
public class JwtConfig
{
    private final AppConfig appConfig;
    @Bean
    public Key key()
    {
        return Keys.hmacShaKeyFor(appConfig.getJwtSecret().getBytes());
    }
}
