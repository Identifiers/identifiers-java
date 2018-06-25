package io.identifiers;

public interface Identifier<T> {

    IdentifierType type();

    T value();

    String toDataString();

    String toHumanString();
}
