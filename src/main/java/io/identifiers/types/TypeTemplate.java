package io.identifiers.types;

import io.identifiers.IdentifierType;

public interface TypeTemplate<T> {

    T initialValue(T value);

    IdentifierType type();

    T value(T value);

    boolean isValueMutable();

    String toDataString(T value);

    String toHumanString(T value);

    String valueString(T value);

    int valueHashCode(T value);

    boolean valuesEqual(T value1, T value2);
}
