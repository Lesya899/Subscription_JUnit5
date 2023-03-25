package org.lesya.mapper;


import lombok.NoArgsConstructor;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;
import org.lesya.entity.Status;
import org.lesya.entity.Subscription;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateSubscriptionMapper implements Mapper<CreateSubscriptionDto, Subscription> {

    private static final CreateSubscriptionMapper INSTANCE = new CreateSubscriptionMapper();

    public static CreateSubscriptionMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Subscription map(CreateSubscriptionDto object) {
        return Subscription.builder()
                .userId(object.getUserId())
                .name(object.getName())
                .provider(Provider.findByNameOpt(object.getProvider()).orElse(null))
                .expirationDate(object.getExpirationDate())
                .status(Status.ACTIVE)
                .build();
    }
}
