package org.lesya.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lesya.dao.SubscriptionDao;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;
import org.lesya.entity.Status;
import org.lesya.entity.Subscription;
import org.lesya.integration.IntegrationTestBase;
import org.lesya.mapper.CreateSubscriptionMapper;
import org.lesya.validator.CreateSubscriptionValidator;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


class SubscriptionServiceTestIT extends IntegrationTestBase {

    private  SubscriptionDao subscriptionDao;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void init() {
        subscriptionDao = SubscriptionDao.getInstance();
        subscriptionService = new SubscriptionService(
                subscriptionDao,
                CreateSubscriptionMapper.getInstance(),
                CreateSubscriptionValidator.getInstance(),
                Clock.systemUTC()
        );
    }

    @Test
    void upsert() {
        Subscription subscription = subscriptionDao.insert(getSubscription(5));
        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        Subscription actualResult = subscriptionService.upsert(dto);
        assertThat(actualResult).isEqualTo(subscription);
    }

    @Test
    void cancel() {
        Subscription subscription = getSubscription(5);
        subscriptionDao.insert(subscription);
        subscriptionService.cancel(subscription.getId());
        assertThat(subscriptionDao.findById(subscription.getId()).get().getStatus()).isEqualTo(Status.CANCELED);
    }

    @Test
    void expire() {
        Subscription subscription = getSubscription(5);
        subscriptionDao.insert(subscription);
        subscriptionService.expire(subscription.getId());
        assertThat(subscriptionDao.findById(subscription.getId()).get().getStatus()).isEqualTo(Status.EXPIRED);
    }

    private CreateSubscriptionDto getCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(5)
                .name("Svetlana")
                .provider(Provider.GOOGLE.name())
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    private Subscription getSubscription(int userId) {
        return Subscription.builder()
                .userId(userId)
                .name("Svetlana")
                .provider(Provider.GOOGLE)
                .expirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.ACTIVE)
                .build();
    }
}