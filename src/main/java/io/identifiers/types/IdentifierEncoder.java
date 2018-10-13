package io.identifiers.types;

import io.identifiers.IdentifierType;

public interface IdentifierEncoder<T> {

    IdentifierType getType();

    String toDataString(T value);

    String toHumanString(T value);
}
