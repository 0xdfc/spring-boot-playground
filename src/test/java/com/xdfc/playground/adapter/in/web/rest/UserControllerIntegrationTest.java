package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.constant.AuthConstants;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import com.xdfc.playground.adapter.in.web.rest.route.UserControllerRoutes;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.configuration.TestUtilityConfiguration;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

@Accessors(chain = true)
@Setter
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

    private List<UserEntity> users;
    private UserEntity userA, userB;

    private String authBearer;

    @BeforeEach
    public void initialiseUserControllerTestData() {
        this.setUsers(this.factory.createAndPersistMany((short) 2))
            .setUserA(this.users.getFirst())
            .setUserB(this.users.getLast());

        this.setAuthBearer(AuthConstants.BearerPrefix.concat(
            this.manager.generateTokenFor(this.userA.getUsername())
        ));
    }

    @Test
    public void aUserCanViewTheirOwnUserById() {
        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
            .header(AuthConstants.AuthHeader, this.authBearer)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ListingUserDTO.class)
            .isEqualTo(
                this.delegate.getMapper().toDto(this.userA)
            );
    }

    // This test perhaps shouldn't really exist or should but...
    // the problem I have with it is that it is testing the web
    // security config. On the other hand, the test ensures that
    // regardless of future changes elsewhere, this will tell us
    // that expectations are or are not met.
    @Test
    public void aGuestCannotViewAUserById() {
        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void oneUserCannotFindAnotherById() {
        final String userBToken = this.manager
            .generateTokenFor(this.userB.getUsername());

        this.client.get()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
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
        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
            .header(AuthConstants.AuthHeader, this.authBearer)
            .exchange()
            .expectStatus()
            .isNoContent();
    }

    @Test
    public void oneUserCannotDeleteAnotherById() {
        final String userBToken = this.manager
            .generateTokenFor(this.userB.getUsername());

        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
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
        this.client.delete()
            .uri(UserControllerRoutes.getUserByIdEndpoint(this.userA.getId()))
            .exchange()
            .expectStatus()
            .isForbidden();
    }
}
