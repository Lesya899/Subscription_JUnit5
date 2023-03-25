package org.lesya.exception;

import org.lesya.validator.Error;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    @Getter
    private final List<Error> errors;
}
