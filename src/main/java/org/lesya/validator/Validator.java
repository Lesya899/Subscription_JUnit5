package org.lesya.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
