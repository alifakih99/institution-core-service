package com.itg.institution.shared.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("hash")
public class AppConfig
{
    private String privateKey;
    private String jwtSecret;
    private int jwtExpiration;
    private int refreshExpiration;
}
