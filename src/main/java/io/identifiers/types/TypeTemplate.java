package io.identifiers.types;

import io.identifiers.IdentifierType;
import java.util.function.Supplier;

public interface TypeTemplate<T> {

    IdentifierType type();

    String toDataString(T value);

    String toHumanString(T value);

    Supplier<T> valueSupplier(T data);
}
