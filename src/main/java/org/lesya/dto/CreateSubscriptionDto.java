package org.lesya.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class CreateSubscriptionDto {
    Integer userId;
    String name;
    String provider;
    Instant expirationDate; //дата окончания срока
}
