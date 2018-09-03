package io.identifiers.types;

import io.identifiers.IdentifierType;

public interface TypeTemplate<T> {

    IdentifierType type();

    T value(T value);

    default boolean isValueMutable() {
        return false;
    }

    String toDataString(T value);

    String toHumanString(T value);

    String valueString(T value);

    int valueHashCode(T value);

    boolean valuesEqual(T value1, T value2);
}
