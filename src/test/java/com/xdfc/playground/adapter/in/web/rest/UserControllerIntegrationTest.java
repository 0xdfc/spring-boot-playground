package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.constant.AuthConstants;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import com.xdfc.playground.adapter.in.web.rest.route.UserControllerRoutes;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.configuration.TestUtilityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@Import(TestUtilityConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class UserControllerIntegrationTest {
    @Autowired
    private RestTestClient client;

    @Autowired
    private TestUserFactory factory;

    @Autowired
    private JwtTokenManager manager;

    @Autowired
    private UserRequirementsDelegate delegate;

    @Test
    public void aUserCanViewTheirOwnUserById() {
        final UserEntity user = this.factory.createAndPersist();

        final String token = this.manager.generateTokenFor(user.getUsername());

        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(user.getId()))
            .header(
                AuthConstants.AuthHeader,
                AuthConstants.BearerPrefix.concat(token)
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ListingUserDTO.class)
            .isEqualTo(
                this.delegate.getMapper().toDto(user)
            );
    }

    // This test perhaps shouldn't really exist or should but...
    // the problem I have with it is that it is testing the web
    // security config. On the other hand, the test ensures that
    // regardless of future changes elsewhere, this will tell us
    // that expectations are or are not met.
    @Test
    public void aGuestCannotViewAUserById() {
        final UserEntity user = this.factory.createAndPersist();

        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(user.getId()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void oneUserCannotFindAnotherById() {
        final UserEntity
            userA = this.factory.createAndPersist(),
            userB = this.factory.createAndPersist();

        final String userBToken = this.manager.generateTokenFor(userB.getUsername());

        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(userA.getId()))
            .header(
                    AuthConstants.AuthHeader,
                    AuthConstants.BearerPrefix.concat(userBToken)
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void aUserCanDeleteTheirOwnAccount() {
        final UserEntity user = this.factory.createAndPersist();

        final String token = this.manager.generateTokenFor(user.getUsername());

        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(user.getId()))
            .header(
                    AuthConstants.AuthHeader,
                    AuthConstants.BearerPrefix.concat(token)
            )
            .exchange()
            .expectStatus()
            .isNoContent();
    }

    @Test
    public void oneUserCannotDeleteAnotherById() {
        final UserEntity
                userA = this.factory.createAndPersist(),
                userB = this.factory.createAndPersist();

        final String userBToken = this.manager.generateTokenFor(userB.getUsername());

        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(userA.getId()))
            .header(
                    AuthConstants.AuthHeader,
                    AuthConstants.BearerPrefix.concat(userBToken)
            )
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void aGuestCannotDeleteAnotherUserById() {
        final UserEntity user = this.factory.createAndPersist();

        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(user.getId()))
            .exchange()
            .expectStatus()
            .isForbidden();
    }
}
