package com.sse.upgrade.security;

import com.sse.upgrade.model.User;
import com.sse.upgrade.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    @Autowired CookieSecurityContextRepository cookieRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String name = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        System.out.println(name+password);
//        Token token = userService.login(name, password);
//        token.setAuthenticated(true);
//        return token;
        List<GrantedAuthority> g = new LinkedList<>();
        g.add(User.Role.PROFESSOR);
        String secret = UUID.randomUUID().toString();
        cookieRepository.startSession(secret);
        return new CookieAuthentication(new User("Günther", User.Role.PROFESSOR), secret, g);
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
