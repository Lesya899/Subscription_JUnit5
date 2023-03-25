package org.lesya.validator;

import org.junit.jupiter.api.Test;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class CreateSubscriptionValidatorTest {

    private final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();


    @Test
    void shouldPassValidation() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        ValidationResult actualResult = validator.validate(dto);
        assertFalse(actualResult.hasErrors());
    }

    @Test
    void invalidUserId() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(100);
    }

    @Test
    void invalidName() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(8)
                .name(null)
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(101);
    }

    @Test
    void invalidProvider() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(8)
                .name("Svetlana")
                .provider("fake")
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(102);
    }

    @Test
    void invalidExpirationDate() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(null)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103);
    }

    @Test
    void invalidUserIdNameProviderExpirationDate() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name(null)
                .provider("fake")
                .expirationDate(Instant.parse("2022-03-23T19:30:30.00Z"))
                .build();

        ValidationResult actualResult = validator.validate(dto);
        List<Integer> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).contains(100, 101, 102,103);
    }

}