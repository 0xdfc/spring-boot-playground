package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.dto.user.CreateUserDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.user.LoginDTO;
import com.xdfc.playground.adapter.in.web.rest.route.AuthControllerRoutes;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.generator.FakeDataGenerator;
import com.xdfc.playground.utility.TestUtilityConfiguration;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

@Import(TestUtilityConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class AuthControllerIntegrationTest {
    @Autowired
    private JwtTokenManager tokenManager;

    @Autowired
    private Faker faker;

    @Autowired
    private RestTestClient client;

    @Autowired
    private FakeDataGenerator generator;

    @Autowired
    private TestUserFactory userFactory;

    protected RestTestClient.ResponseSpec registerUserThroughClientWith(
        final CreateUserDTO dto
    ) {
        return this.client.post()
            .uri(AuthControllerRoutes.REGISTRATION_ENDPOINT)
            .body(dto)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
    }

    @Test
    public void shouldRegisterAUserAndBecomeAuthenticatedAsSaidUser() {
        final CreateUserDTO dto = CreateUserDTO.make(
            this.generator.generateValidEmailAddress(),
            this.generator.generateValidUserPassword()
        );

        this.registerUserThroughClientWith(dto)
            .expectStatus()
            .isCreated()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectCookie()
            .exists(
                this.tokenManager.getTokenCookieName()
            )
            .expectBody();
    }

    @Test
    public void registrationWithInvalidCredentialsGetsBlocked() {
        final String blank = " ".repeat(10);

        final List<CreateUserDTO> dtos = List.of(
            CreateUserDTO.make(
                this.faker.name()
                    .firstName(),
                this.generator.generateValidUserPassword()
            ),
            CreateUserDTO.make(blank, this.generator.generateValidUserPassword()),


            CreateUserDTO.make(this.generator.generateValidEmailAddress(), blank),
            CreateUserDTO.make(
                this.generator.generateValidEmailAddress(),
                this.faker.text().text(7)
            ),
            CreateUserDTO.make(
                this.generator.generateValidEmailAddress(),
                this.faker.text().text(65)
            )
        );

        dtos.forEach(
            (dto) -> this.registerUserThroughClientWith(dto)
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
            );
    }

    @Test
    public void registeringWithDuplicateEmailsBailsWithValidationErrors() {
        final String email = this.generator.generateValidEmailAddress();

        final CreateUserDTO
            initial = CreateUserDTO.make(email, this.generator.generateValidUserPassword()),
            alternate = CreateUserDTO.make(email, this.generator.generateValidUserPassword());

        this.registerUserThroughClientWith(initial).expectStatus().isCreated();

        this.registerUserThroughClientWith(alternate)
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldBeAbleToLoginWithAnExistingUser() {
        String email = this.generator.generateValidEmailAddress(),
            password = this.generator.generateValidUserPassword();

        this.userFactory.createAndPersist(email, password);

        this.client.post()
            .uri(AuthControllerRoutes.LOGIN_ENDPOINT)
            .body(new LoginDTO(email, password))
            .exchange()
            .expectStatus()
            .isOk()
            .expectCookie()
            .exists(this.tokenManager.getTokenCookieName());
    }

}
