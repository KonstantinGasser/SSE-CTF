package com.sse.upgrade.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CookieAuthentication extends UsernamePasswordAuthenticationToken {
    public CookieAuthentication(Object principal, Object credentials, Collection<GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    @Override
    public void eraseCredentials() {

    }
}
