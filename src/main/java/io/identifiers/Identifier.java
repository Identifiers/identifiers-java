package io.identifiers;

public interface Identifier<T> {
    enum TYPE {
        STRING,
        BOOLEAN,
        INTEGER,
        FLOAT,
        LONG,
        BYTES
    }

    TYPE type();

    T value();

    String toDataString();

    String toHumanString();
}
