package com.xdfc.playground.adapter.in.web.security.jwt;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
final public class JwtAuthContextHandler {

    public void authorizeUserWithinSpringSecurityContext(
        final HttpServletRequest request,
        final UserEntity user
    ) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // ? Although the authorities parameter is nullable, it seems to be
        // ? required even when authorities are not actually put to use.
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        context.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(context);
    }
}
