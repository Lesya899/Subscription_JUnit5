package org.lesya.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
