package com.xdfc.playground.generator;

import net.datafaker.Faker;
import net.datafaker.providers.base.Text;
import org.springframework.beans.factory.annotation.Autowired;

import static net.datafaker.providers.base.Text.*;

final public class FakeDataGenerator {
    @Autowired
    private Faker faker;

    public String generateValidEmailAddress() {
        return this.faker.internet().emailAddress();
    }

    public String generateValidUserPassword() {
        return this.faker.text().text(
            Text.TextSymbolsBuilder
                .builder()
                .len(8)
                .with(EN_UPPERCASE, 2)
                .with(DIGITS, 2)
                .with(EN_LOWERCASE, 4)
                .build()
        );
    }
}
