package com.sse.upgrade.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PasswordAuthenticationProvider passAuthProvider;
    @Autowired
    CookieSecurityContextRepository cookieSecurityContextRepository;


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(passAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().securityContext().securityContextRepository(cookieSecurityContextRepository)
                .and().logout().permitAll().deleteCookies(UserCookie.NAME)
                .and().formLogin()
                .and().authorizeRequests()
                .antMatchers("/login", "/h2-console/**", "/users/registration", "static/**", "resources/**", "resources/static/**").permitAll()
                .antMatchers("/**").authenticated();
    }
}
