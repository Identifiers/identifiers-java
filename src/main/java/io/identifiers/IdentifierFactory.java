package io.identifiers;

/**
 * Combines Factory interfaces for an identifier type.
 *
 * @param <T> the value type of the created identifier.
 */
public interface IdentifierFactory<T> extends SingleIdentifierFactory<T>, ListIdentifierFactory<T>, MapIdentifierFactory<T> {
}
