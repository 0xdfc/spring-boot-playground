package com.xdfc.playground.utility;

import com.xdfc.playground.factory.TestUserFactory;
import com.xdfc.playground.generator.FakeDataGenerator;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

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
}
