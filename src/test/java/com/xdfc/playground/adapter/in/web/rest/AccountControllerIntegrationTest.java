package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.constant.AuthConstants;
import com.xdfc.playground.adapter.in.web.rest.dto.account.CreateAccountDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.account.ListingAccountDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.account.ListingAccountsDTO;
import com.xdfc.playground.adapter.in.web.rest.route.AccountControllerRoutes;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import com.xdfc.playground.domain.service.AccountService;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.configuration.TestUtilityConfiguration;
import com.xdfc.playground.generator.ConcurrentRequestGenerator;
import com.xdfc.playground.utility.ResponseSpecUtility;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestUtilityConfiguration.class)
@Setter
@Accessors(chain = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class AccountControllerIntegrationTest {
    @Autowired
    private RestTestClient client;

    @Autowired
    private TestUserFactory factory;

    @Autowired
    private ConcurrentRequestGenerator concurrentRequestGenerator;

    @Autowired
    private ResponseSpecUtility responseSpecUtility;

    @Autowired
    private JwtTokenManager manager;

    @Autowired
    private AccountService accountService;

    private String authBearer;
    private UserEntity requestAuthor;
    private String currencyCode;

    @BeforeEach
    public void initialiseAccountControllerTestData() {
        this.setRequestAuthor(this.factory.createAndPersist())
            .setCurrencyCode(CurrencyCode.AED.name())
            .setAuthBearer(AuthConstants.BearerPrefix.concat(
                this.manager.generateTokenFor(
                    this.requestAuthor.getUsername()
                )
            ));
    }

    private RestTestClient.ResponseSpec createAccountRequest() {
        return this.client.post().uri(AccountControllerRoutes.ROOT_ENDPOINT)
            .header(AuthConstants.AuthHeader, this.authBearer)
            .accept(MediaType.APPLICATION_JSON)
            .body(new CreateAccountDTO(this.currencyCode))
            .exchange();
    }

    @Test
    public void ensureAUserCanIndexTheirAccounts() {
        this.client.get()
            .uri(AccountControllerRoutes.ROOT_ENDPOINT)
            .header(AuthConstants.AuthHeader, this.authBearer)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ListingAccountsDTO.class)
            .isEqualTo(
                new ListingAccountsDTO(new HashSet<>())
            );
    }

    @Test
    public void ensureAGuestCantCreateAnAccount() {
        this.client.post()
            .uri(AccountControllerRoutes.ROOT_ENDPOINT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void ensureAUserCanCreateAnAccountForAKnownCurrency() {
        this.createAccountRequest().expectStatus().isCreated().expectBody(
            ListingAccountDTO.class
        );
    }

    @Test
    public void ensureAUserCantCreateAnAccountForAnUnknownCurrency() {
        this.setCurrencyCode("UKN");

        this.createAccountRequest()
            .expectStatus().isBadRequest();
    }

    @Test
    public void ensureOnlyOneAccountIsCreatedWithAsyncRequests() {
        final List<HttpStatusCode> responses =
            this.responseSpecUtility.convertResponsesToStatusCodes(
                this.concurrentRequestGenerator.generate(
                    this::createAccountRequest
                )
            );

        assertThat(this.accountService.find(this.requestAuthor))
            .hasSize(1);

        List<Function<HttpStatusCode, Boolean>> assertable = List.of(
            HttpStatusCode::is2xxSuccessful,
            HttpStatusCode::is4xxClientError
        );

        assertable.forEach(
            (callable) -> this
                .responseSpecUtility
                .assertResponseCodesFromExtractorHasOneTrueAndOneFalse(
                    responses,
                    callable
                )
        );
    }
}
