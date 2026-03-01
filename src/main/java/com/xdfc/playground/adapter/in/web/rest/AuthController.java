package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.dto.user.CreateUserDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.auth.JwtTokenDto;
import com.xdfc.playground.adapter.in.web.rest.dto.user.LoginDTO;
import com.xdfc.playground.adapter.in.web.rest.exception.NotFoundHttpException;
import com.xdfc.playground.adapter.in.web.rest.route.AuthControllerRoutes;
import com.xdfc.playground.adapter.in.web.rest.service.HttpAuthService;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthControllerRoutes.ROOT_ENDPOINT)
public class AuthController {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRequirementsDelegate users;

    @Autowired
    private HttpAuthService httpAuthService;

    @PostMapping(AuthControllerRoutes.REGISTRATION_SEGMENT)
    @ResponseStatus(HttpStatus.CREATED)
    public ListingUserDTO create(
        @NonNull final HttpServletResponse response,
        @Valid @RequestBody final CreateUserDTO user
    ) {
        final UserEntity entity = this.users.getMapper().toUser(user);

        entity.setPassword(this.encoder.encode(entity.getPassword()));

        final ListingUserDTO respondingWith = this.users.getMapper()
            .toDto(this.users.getService().save(entity));

        this.httpAuthService.authorise(response, entity.getUsername());

        return respondingWith;
    }

    // TODO ->> this endpoint should be rate-limited, but,
    // TODO ^->> in optimal setups this would be offloaded onto
    // TODO ^->> services like the Spring Cloud Gateway etc
    @PostMapping(AuthControllerRoutes.LOGIN_SEGMENT)
    public JwtTokenDto login(
        @NonNull final HttpServletResponse response,
        @Valid @RequestBody final LoginDTO credentials
    ) {
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.username(),
                credentials.password()
            )
        );

        final UserEntity user = this.users
            .getService()
            .findWithLockByUsername(credentials.username())
            .orElseThrow(
                NotFoundHttpException::new
            );

        return this.httpAuthService.authorise(response, credentials.username());
    }

    @PostMapping(AuthControllerRoutes.FORGOT_PASSWORD_SEGMENT)
    public void forgot() {
        // TODO ->>
    }

    @PostMapping(AuthControllerRoutes.RESET_PASSWORD_SEGMENT)
    public void reset() {
        // TODO ->>
    }
}
