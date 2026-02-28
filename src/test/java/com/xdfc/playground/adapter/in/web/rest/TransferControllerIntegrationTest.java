package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.constant.AccountConstants;
import com.xdfc.playground.adapter.in.web.rest.constant.AuthConstants;
import com.xdfc.playground.adapter.in.web.rest.dto.account.AccountTransferDto;
import com.xdfc.playground.adapter.in.web.rest.route.TransferControllerRoutes;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.CurrencyRepository;
import com.xdfc.playground.configuration.TestUtilityConfiguration;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import com.xdfc.playground.domain.service.AccountService;
import com.xdfc.playground.factory.TestAccountFactory;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.generator.ConcurrentRequestGenerator;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestUtilityConfiguration.class)
@Setter
@Accessors(chain = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TransferControllerIntegrationTest {
    @Autowired
    private RestTestClient client;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestAccountFactory accountFactory;

    @Autowired
    private JwtTokenManager manager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ConcurrentRequestGenerator concurrentRequestGenerator;

    private List<UserEntity> users;
    private List<AccountEntity> accounts;

    private UserEntity transactionAuthor;
    private AccountEntity source;
    private AccountEntity destination;
    private BigDecimal transferTotal;

    private String authBearer;

    @BeforeEach
    public void initialiseTransferControllerTestData() {
        this.setUsers(this.userFactory.createAndPersistMany((short) 2))
            .setAccounts(this.accountFactory.createAndPersistMany(users))

            .setSource(this.accounts.getFirst())
            .setDestination(this.accounts.getLast())

            .setTransferTotal(AccountConstants.AccountStartingBalance)
            .setTransactionAuthor(this.users.getFirst())

            .setAuthBearer(AuthConstants.BearerPrefix.concat(
                this.manager.generateTokenFor(
                    this.transactionAuthor.getUsername()
                )
        ));
    }

    private RestTestClient.ResponseSpec makeTransferRequest() {
        final AccountTransferDto dto = new AccountTransferDto(
            this.source.getAddress(),
            this.destination.getAddress(),
            this.transferTotal
        );

        return this.client
            .post()
            .uri(TransferControllerRoutes.ROOT_ENDPOINT)
            // There must be a better way to do this while avoiding
            // wrapping this in a call to a service... AOP?
            // TODO ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            // We could wrap this logic into an "authorised" and an
            // "unauthorised" request in a request spec utility
            .header(AuthConstants.AuthHeader, this.authBearer)
            .accept(MediaType.APPLICATION_JSON)
            .body(dto)
            .exchange();
    }

    private void assertAccountBalance(
        final AccountEntity account,
        final String expectedBalance
    ) {
        final AccountEntity refreshed = this.accountService.find(account.getId())
            .orElseThrow();

        assertThat(refreshed.getBalance())
            .isEqualByComparingTo(expectedBalance);
    }

    private void updateAccountBalance(
        final AccountEntity account,
        final String balance
    ) {
        account.setBalance(
            new BigDecimal(balance)
        );

        this.accountService.save(account);
    }

    @Test
    public void ensureUserCanTransferFromTheirAccountToAnotherUsersAccount() {
        this.makeTransferRequest().expectStatus().isOk();

        this.assertAccountBalance(this.source, "0");
        this.assertAccountBalance(this.destination, "200");
    }

    @Test
    public void ensureAUserCannotTransferMoreThanWhatTheyHave() {
        this.setTransferTotal(new BigDecimal("101"));

        this.makeTransferRequest().expectStatus().isBadRequest();

        this.assertAccountBalance(source, "100");
        this.assertAccountBalance(destination, "100");
    }

    @Test
    public void ensureAUserCantDoubleTransferThroughConcurrentRequests() {
        this.concurrentRequestGenerator.generate(
                this::makeTransferRequest
        );

        this.assertAccountBalance(this.source, "0");
        this.assertAccountBalance(this.destination, "200");
    }

    @Test
    public void ensureAUserCantTransferToUnknownAccounts() {
        this.setDestination(new AccountEntity().setAddress(
            UUID.randomUUID().toString()
        ));

        this.makeTransferRequest().expectStatus().isNotFound();
    }

    @Test
    public void ensureAGuestHasNoAccessToTheTransferEndpoint() {
        this.client.post()
            .uri(TransferControllerRoutes.ROOT_ENDPOINT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    public void ensureOutOfBoundsTransferSumValidationRulesAreApplied() {
        this.updateAccountBalance(this.source, "99999999");

        List.of("0", "0.001", "100000.001", "100001.00").forEach(
            (sum) -> {
                this.setTransferTotal(new BigDecimal(sum));

                this.makeTransferRequest().expectStatus()
                    .isBadRequest();
            }
        );
    }

    @Test
    public void ensureInBoundTransferSumsAreSendable() {
        this.updateAccountBalance(this.source, "99999999");

        List.of("0.01", "100000.00", "99999.99").forEach(
            (sum) -> {
                this.setTransferTotal(new BigDecimal(sum));

                this.makeTransferRequest().expectStatus()
                    .isOk();
            }
        );
    }

    @Test
    public void ensureTransferringToAnAccountWithADifferentCurrencyFails() {
        this.destination.setCurrency(
            this.currencyRepository.findByCode(CurrencyCode.USD.name())
        );

        this.accountService.save(this.destination);

        this.makeTransferRequest().expectStatus().isBadRequest();
    }

    @Test
    public void ensureTransferringFromAnotherUsersAccountFails() {
        this.setDestination(this.source);
        this.setSource(this.accounts.getLast());

        this.makeTransferRequest().expectStatus().isNotFound();
    }

    /**
     * In our case this doesn't have any meaning whatsoever. But, in a
     * real scenario, promos and other benefits based on total account
     * spend would need to be protected.
     */
    @Test
    public void ensureTransferringToAndFromTheSameAddressIsImpossible() {
        this.setDestination(this.source);

        // Hits a second attempt to read the same record and bails
        this.makeTransferRequest()
            .expectStatus()
            .isBadRequest();
    }

    @Test
    public void ensureAUserCantReceiveIfTheyHaveReachedTheAccountLimit() {
        this.updateAccountBalance(
            this.destination,
            AccountConstants.MaximumAccountBalance.toString()
        );

        this.makeTransferRequest().expectStatus().isBadRequest();
    }
}
