package com.xdfc.playground.adapter.in.web.rest.service;

import com.xdfc.playground.adapter.in.web.rest.dto.auth.JwtTokenDto;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class HttpAuthService {
    @Autowired
    private JwtTokenManager jwtTokenManager;

    final public JwtTokenDto authorise(
        @NonNull final HttpServletResponse response,
        @NonNull final String username
    ) {
        final JwtTokenDto container = new JwtTokenDto(
            this.jwtTokenManager.generateTokenFor(username)
        );

        final String cookieName = this.jwtTokenManager.getTokenCookieName();

        final ResponseCookie cookie = ResponseCookie
            .from(cookieName, container.token())
            .httpOnly(true)
            .secure(true)
            .sameSite(Cookie.SameSite.STRICT.toString())
            .maxAge(
                this.jwtTokenManager.getTokenExpirationTime().toSeconds()
            )
            .path("/")
            .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return container;
    }
}
