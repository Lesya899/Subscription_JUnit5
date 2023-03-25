package org.lesya.dao;

import org.junit.jupiter.api.Test;
import org.lesya.entity.Provider;
import org.lesya.entity.Status;
import org.lesya.entity.Subscription;
import org.lesya.integration.IntegrationTestBase;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubscriptionDaoTestIT extends IntegrationTestBase {

    private final SubscriptionDao subscriptionDao = SubscriptionDao.getInstance();


    @Test
    void findAll() {
        Subscription subscriptionFirst = subscriptionDao.insert(getSubscription(4));
        Subscription subscriptionSecond = subscriptionDao.insert(getSubscription(5));
        Subscription subscriptionThird = subscriptionDao.insert(getSubscription(6));
        List<Subscription> actualResult = subscriptionDao.findAll();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).contains(subscriptionFirst, subscriptionSecond, subscriptionThird);
    }

    @Test
    void findById() {
        Subscription subscription = subscriptionDao.insert(getSubscription(4));
        Optional<Subscription> actualResult = subscriptionDao.findById(subscription.getId());
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(subscription);
    }

    @Test
    void deleteExistingEntity() {
        Subscription subscription = subscriptionDao.insert(getSubscription(4));
        boolean actualResult = subscriptionDao.delete(subscription.getId());
        assertTrue(actualResult);
   }

    @Test
    void deleteNotExistingEntity() {
        subscriptionDao.insert(getSubscription(4));
        boolean actualResult = subscriptionDao.delete(10);
        assertFalse(actualResult);
    }

    @Test
    void update() {
        Subscription subscription = getSubscription(4);
        subscriptionDao.insert(subscription);
        subscription.setName("Sveta");
        subscription.setExpirationDate(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS));
        subscriptionDao.update(subscription);
        Subscription updatedSubscription = subscriptionDao.findById(subscription.getId()).get();
        assertThat(updatedSubscription).isEqualTo(subscription);
    }

    @Test
    void insert() {
        Subscription subscription = getSubscription(4);
        Subscription actualResult = subscriptionDao.insert(subscription);
        assertNotNull(actualResult.getId());
    }

    @Test
    void findByUserId() {
        Subscription subscription = subscriptionDao.insert(getSubscription(6));
        List<Subscription> actualResult = subscriptionDao.findByUserId(subscription.getUserId());
        assertThat(actualResult).contains(subscription);
    }

    @Test
    void shouldNotFindUserIdIfSubscriptionDoesNotExist() {
        subscriptionDao.insert(getSubscription(7));
        List<Subscription> actualResult = subscriptionDao.findByUserId(9);
        assertThat(actualResult).isEmpty();
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