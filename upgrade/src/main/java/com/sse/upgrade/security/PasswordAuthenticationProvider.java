package com.sse.upgrade.security;

import com.sse.upgrade.model.User;
import com.sse.upgrade.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public class PasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    @Autowired CookieSecurityContextRepository cookieRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.login(name, password);
        String secret = UUID.randomUUID().toString();
        cookieRepository.startSession(secret);
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        return new CookieAuthentication(user, secret, authorities);
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
