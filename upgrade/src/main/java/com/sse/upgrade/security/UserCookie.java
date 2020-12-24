package com.sse.upgrade.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class UserCookie extends Cookie {

    public static final String NAME = "UserSession";
    private static final String PATH = "/";

    private final Payload payload;

    public UserCookie(User userInfo, String secret) {
        super(NAME, "");
        this.payload = new Payload(secret, userInfo.getUsername(), userInfo.getRoles());
        this.setPath(PATH);
        this.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).toSeconds());
        this.setHttpOnly(true);
    }

    public UserCookie(Cookie cookie) {
        super(NAME, "");

        if (!NAME.equals(cookie.getName()))
            throw new IllegalArgumentException("No " + NAME + " Cookie");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String value = new String(Base64.getDecoder().decode(cookie.getValue()));
            this.payload = objectMapper.readValue(value, Payload.class);
        } catch (JsonProcessingException e) {
            throw new BadCredentialsException("Bad Cookie");
        }

        this.setPath(cookie.getPath());
        this.setMaxAge(cookie.getMaxAge());
        this.setHttpOnly(cookie.isHttpOnly());
    }

    @Override
    public String getValue() {
        return payload.serialize();
    }

    public User getUserInfo() {
        return new User(payload.username, payload.roles);
    }

    public String getSecret() {
        return payload.secret;
    }

    private static class Payload {
        public String username;
        public Collection<User.Role> roles;
        public String secret;

        public Payload() {}

        public Payload(String secret, String username, Collection<User.Role> roles) {
            this.secret = secret;
            this.username = username;
            this.roles = roles;
        }

        public String serialize() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(this);
                return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}