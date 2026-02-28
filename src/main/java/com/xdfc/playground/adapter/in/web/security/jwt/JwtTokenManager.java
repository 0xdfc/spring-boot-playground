package com.xdfc.playground.adapter.in.web.security.jwt;

import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Getter
@Setter
@Validated
@ConfigurationProperties("spring.application.jwt")
final public class JwtTokenManager {
    @NotEmpty
    private String tokenGenerationKey;

    @NotNull
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration tokenExpirationTime;

    @NotEmpty
    private String tokenCookieName;

    public String generateTokenFor(final String username) {
        final Instant now = Instant.now();

        return Jwts.builder()
            .signWith(this.tokenSignatureKey())
            .subject(username)
            .expiration(
                Date.from(now.plus(this.tokenExpirationTime))
            )
            .issuedAt(Date.from(now))
            .compact();
    }

    public String tokenToUsername(final String token) {
        return this.parser()
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    private SecretKey tokenSignatureKey() {
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(this.tokenGenerationKey)
        );
    }

    private JwtParserBuilder parser() {
        return Jwts.parser().verifyWith(this.tokenSignatureKey());
    }

    public void validateToken(final String token) throws SignatureException {
        this.parser().build().parse(token);
    }
}
