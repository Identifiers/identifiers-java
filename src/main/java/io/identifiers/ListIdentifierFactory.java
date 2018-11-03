package io.identifiers;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collector;

/**
 * Create typed List Identifier instances.
 *
 * @param <T> the value type of the identifiers in the list
 */
public interface ListIdentifierFactory<T> {

    /**
     * Create from a Collection of values.
     *
     * @param values the collection of values
     * @return a list identifier containing the values.
     */
    ListIdentifier<T> createList(Collection<T> values);

    /**
     * Create from an Iterator of values.
     *
     * @param values an iterator of values.
     * @return a list identifier containing the values.
     */
    ListIdentifier<T> createList(Iterator<T> values);

    /**
     * Create from a varargs array of values.
     *
     * @param values the array of values.
     * @return a list identifier containing the values.
     */
    @SuppressWarnings("unchecked")
    ListIdentifier<T> createList(T... values);

    /**
     * Provides a Java Stream collector that consumes a stream of values and produces
     * a list identifier containing the values.
     *
     * @return A Collector suitable for use with {@link java.util.stream.Stream#collect(Collector)}.
     */
    Collector<T, ?, ListIdentifier<T>> toListIdentifier();
}
