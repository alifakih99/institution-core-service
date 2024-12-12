package com.itg.institution.shared.security;

import com.itg.institution.shared.configuration.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder
{
    @Autowired
    private AppConfig appConfig;

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword)
    {
        String modifiedPassword = rawPassword.toString() + appConfig.getPrivateKey();
        return delegate.encode(modifiedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword)
    {
        String extractedPassword = encodedPassword.replace(appConfig.getPrivateKey(), "");
        return delegate.matches(rawPassword + appConfig.getPrivateKey(), extractedPassword);
    }
}

