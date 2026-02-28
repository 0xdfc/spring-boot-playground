package com.xdfc.playground.configuration;

import com.xdfc.playground.factory.TestAccountFactory;
import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.generator.ConcurrentRequestGenerator;
import com.xdfc.playground.generator.FakeDataGenerator;
import com.xdfc.playground.utility.ResponseSpecUtility;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.concurrent.CompletableFuture;

@TestConfiguration
public class TestUtilityConfiguration {
    @Bean
    Faker faker() {
        return new Faker();
    }

    @Bean
    FakeDataGenerator fakeDataGenerator() {
        return new FakeDataGenerator();
    }

    @Bean
    TestUserFactory testUserFactory() {
        return new TestUserFactory();
    }

    @Bean
    TestAccountFactory testAccountFactory() {
        return new TestAccountFactory();
    }

    @Bean
    ConcurrentRequestGenerator concurrentRequestGenerator() {
        return new ConcurrentRequestGenerator();
    }

    @Bean
    ResponseSpecUtility responseSpecUtility() {
        return new ResponseSpecUtility();
    }
}
