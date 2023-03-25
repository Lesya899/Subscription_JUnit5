package org.lesya.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lesya.dao.SubscriptionDao;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;
import org.lesya.entity.Status;
import org.lesya.entity.Subscription;
import org.lesya.exception.ValidationException;
import org.lesya.mapper.CreateSubscriptionMapper;
import org.lesya.validator.CreateSubscriptionValidator;
import org.lesya.validator.Error;
import org.lesya.validator.ValidationResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private CreateSubscriptionValidator createSubscriptionValidator;
    @Mock
    private CreateSubscriptionMapper createSubscriptionMapper;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Spy
    private Clock clock;
    @InjectMocks
    private SubscriptionService subscriptionService;


    @Test
    void upsert() {
        CreateSubscriptionDto createSubscriptionDto = getCreateSubscriptionDto();
        Subscription subscription = getSubscription();
        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(createSubscriptionDto);
        doReturn(List.of(subscription)).when(subscriptionDao).findByUserId(createSubscriptionDto.getUserId());
        doReturn(subscription).when(subscriptionDao).upsert(subscription);
        Subscription actualResult = subscriptionService.upsert(createSubscriptionDto);
        assertThat(subscription).isEqualTo(actualResult);
        verify(subscriptionDao).upsert(subscription);
    }


    @Test
    void cancel() {
        Subscription subscription = getSubscription();
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        subscriptionService.cancel(subscription.getId());
        verify(subscriptionDao).update(subscription);
    }

    @Test
    void expire() {
        Subscription subscription = getSubscription();
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        subscriptionService.expire(subscription.getId());
        verify(subscriptionDao).update(subscription);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(100, "userId is invalid"));
        doReturn(validationResult).when(createSubscriptionValidator).validate(dto);
        assertThrows(ValidationException.class, () -> subscriptionService.upsert(dto));
        verifyNoInteractions(subscriptionDao, createSubscriptionMapper);
    }


    private CreateSubscriptionDto getCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    private Subscription getSubscription() {
        return Subscription.builder()
                .userId(8)
                .name("Svetlana")
                .provider(Provider.GOOGLE)
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.ACTIVE)
                .build();
    }
}