package com.xdfc.playground.adapter.in.web.security.jwt;

import com.xdfc.playground.adapter.in.web.rest.constant.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Service
final public class JwtTokenExtractor {
    private final static int BearerPrefixLength = AuthConstants.BearerPrefix.length();

    @Autowired
    private JwtTokenManager jwtTokenManager;

    private Optional<String> bearerFromHeader(final HttpServletRequest request) {
        final Optional<String> header = Optional.ofNullable(
            request.getHeader(AuthConstants.AuthHeader)
        );

        Optional<String> bearer = Optional.empty();

        if (header.isPresent() && header.get().startsWith(AuthConstants.BearerPrefix)) {
            bearer = Optional.of(header.get().substring(BearerPrefixLength));
        }

        return bearer;
    }

    private Optional<String> bearerFromCookie(final HttpServletRequest request) {
        final Optional<Cookie> httpCookie = Optional.ofNullable(
            WebUtils.getCookie(request, jwtTokenManager.getTokenCookieName())
        );

        Optional<String> token = Optional.empty();

        if (httpCookie.isPresent()) {
            token = Optional.of(httpCookie.get().getValue());
        }

        return token;
    }

    public Optional<String> toToken(final HttpServletRequest request) {
        return this.bearerFromCookie(request).or(
            () -> this.bearerFromHeader(request)
        );
    }
}
