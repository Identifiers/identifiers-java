package io.identifiers;

/**
 * Identifier instance with a typed value.
 *
 * @param <T> the type of the value
 */
public interface Identifier<T> {

    /**
     * Enum type of value.
     *
     * @return the identifier type
     */
    IdentifierType type();

    /**
     * The value of the identifier.
     *
     * @return the value
     */
    T value();

    /**
     * Generates an encoded data string for this identifier.
     *
     * @return The data string representation
     */
    String toDataString();

    /**
     * Generates an encoded human string for this identifier.
     *
     * @return The human string representation
     */
    String toHumanString();
}
