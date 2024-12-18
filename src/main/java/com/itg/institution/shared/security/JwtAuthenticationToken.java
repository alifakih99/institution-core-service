package com.itg.institution.shared.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken
{
    private final Object principal;
    private Object credentials;

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
    {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal()
    {
        return this.principal;
    }

    @Override
    public Object getCredentials()
    {
        return this.credentials;
    }

    @Override
    public void eraseCredentials()
    {
        super.eraseCredentials();
        this.credentials = null;
    }
}
