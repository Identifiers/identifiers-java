package io.identifiers;

/**
 * Create typed Identifier instances.

 * @param <T> the value type of the identifier
 */
public interface SingleIdentifierFactory<T> {

    /**
     * Create an instance of a typed identifier.
     *
     * @param value the value of the identifier
     * @return a new identifier instance
     */
    Identifier<T> create(T value);
}
