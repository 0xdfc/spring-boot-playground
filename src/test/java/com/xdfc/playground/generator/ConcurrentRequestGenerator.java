package com.xdfc.playground.generator;

import jakarta.validation.constraints.NotNull;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConcurrentRequestGenerator {
    public List<RestTestClient.ResponseSpec> generate(
        @NotNull final Supplier<RestTestClient.ResponseSpec> supplier
    ) {
        final CountDownLatch latch = new CountDownLatch(1);

        List<CompletableFuture<RestTestClient.ResponseSpec>> responses = Stream
            .generate(() -> CompletableFuture.supplyAsync(
                () -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) { throw new RuntimeException(e); }

                    return supplier.get();
                }
            ))
            .limit(2)
            .toList();

        latch.countDown();

        return responses.stream().map(CompletableFuture::join).toList();
    }
}
