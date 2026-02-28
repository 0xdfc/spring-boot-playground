package com.xdfc.playground.utility;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseSpecUtility {

    public List<HttpStatusCode> convertResponsesToStatusCodes(
        @NotNull final List<RestTestClient.ResponseSpec> responses
    ) {
        return responses.stream()
            .map((response) -> response.returnResult().getStatus())
            .toList();
    }

    public void assertResponseCodesFromExtractorHasOneTrueAndOneFalse(
        @NotNull final List<HttpStatusCode> codes,
        @NotNull final Function<HttpStatusCode, Boolean> predicate
    ) {
        assertThat(codes).extracting(predicate)
            .containsExactlyInAnyOrder(true, false);
    }
}
