package org.lesya.mapper;

import org.junit.jupiter.api.Test;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;
import org.lesya.entity.Status;
import org.lesya.entity.Subscription;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionMapperTest {

    private final CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void map() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .build();

        Subscription actualResult = mapper.map(dto);

        Subscription expectedResult = Subscription.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE)
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.ACTIVE)
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}