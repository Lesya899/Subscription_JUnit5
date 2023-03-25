package org.lesya.validator;


import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.lesya.dto.CreateSubscriptionDto;
import org.lesya.entity.Provider;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateSubscriptionValidator implements Validator<CreateSubscriptionDto> {

    private static final CreateSubscriptionValidator INSTANCE = new CreateSubscriptionValidator();

    public static CreateSubscriptionValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(CreateSubscriptionDto object) {
        var validationResult = new ValidationResult();
        if (object.getUserId() == null) {
            validationResult.add(Error.of(100, "userId is invalid"));
        }
        if (StringUtils.isBlank(object.getName())) {
            validationResult.add(Error.of(101, "name is invalid"));
        }
        if (Provider.findByNameOpt(object.getProvider()).isEmpty()) {
            validationResult.add(Error.of(102, "provider is invalid"));
        }
        if (object.getExpirationDate() == null || object.getExpirationDate().isBefore(Instant.now())) {
            validationResult.add(Error.of(103, "expirationDate is invalid"));
        }
        return validationResult;
    }
}
