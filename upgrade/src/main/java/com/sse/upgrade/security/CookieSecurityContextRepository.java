package com.sse.upgrade.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class CookieSecurityContextRepository implements SecurityContextRepository {

    private final Set<String> secretStore = new HashSet<>();
    private static final Logger LOG = LoggerFactory.getLogger(CookieSecurityContextRepository.class);
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        requestResponseHolder.setResponse(new SaveToCookieResponseWrapper(request, response));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        try {
            readUserCookie(request).ifPresent(cookie -> {
                User user = cookie.getUserInfo();
                if (!secretStore.contains(cookie.getSecret()))
                    throw new BadCredentialsException("Invalid or expired cookie");
                context.setAuthentication(new UsernamePasswordAuthenticationToken(user, cookie.getSecret(), user.getAuthorities()));
            });
        } catch(BadCredentialsException e) {
            // do nothing
        }

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        SaveToCookieResponseWrapper responseWrapper = (SaveToCookieResponseWrapper) response;
        if (!responseWrapper.isContextSaved()) {
            responseWrapper.saveContext(context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        try {
            return readUserInfoFromCookie(request).isPresent();
        } catch (BadCredentialsException e) {
            return false;
        }
    }

    public void startSession(String secret) {
        this.secretStore.add(secret);
    }

    public void endSession(String secret) {
        this.secretStore.remove(secret);
    }

    private Optional<User> readUserInfoFromCookie(HttpServletRequest request) {
        return readCookieFromRequest(request)
                .map(this::createUserCookie)
                .map(c -> c.getUserInfo());
    }

    private Optional<UserCookie> readUserCookie(HttpServletRequest request) {
        return readCookieFromRequest(request)
                .map(this::createUserCookie);
    }

    private Optional<Cookie> readCookieFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            LOG.debug("No cookies in request");
            return Optional.empty();
        }

        Optional<Cookie> maybeCookie = Stream.of(request.getCookies())
                .filter(c -> UserCookie.NAME.equals(c.getName()))
                .findFirst();

        if (maybeCookie.isEmpty()) {
            LOG.debug("No {} cookie in request", UserCookie.NAME);
        }

        return maybeCookie;
    }

    private UserCookie createUserCookie(Cookie cookie) {
        return new UserCookie(cookie);
    }

    private class SaveToCookieResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
        private final Logger LOG = LoggerFactory.getLogger(SaveToCookieResponseWrapper.class);
        private final HttpServletRequest request;

        SaveToCookieResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
            super(response, true);
            this.request = request;
        }

        @Override
        protected void saveContext(SecurityContext securityContext) {
            HttpServletResponse response = (HttpServletResponse) getResponse();
            Authentication authentication = securityContext.getAuthentication();
            if (authentication == null) {
                return;
            }

            if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
                return;
            }

            if (!(authentication.getPrincipal() instanceof User)) {
                return;
            }

            User userInfo = (User) authentication.getPrincipal();
            String secret = (String) authentication.getCredentials();
            UserCookie cookie = new UserCookie(userInfo, secret);
            cookie.setSecure(request.isSecure());
            response.addCookie(cookie);
        }
    }
}
