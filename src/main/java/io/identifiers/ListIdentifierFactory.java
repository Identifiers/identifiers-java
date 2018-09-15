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

    ListIdentifier<T> createList(Collection<T> values);

    ListIdentifier<T> createList(Iterator<T> values);

    ListIdentifier<T> createList(T... values);

    Collector<T, ?, ListIdentifier<T>> toListIdentifier();
}
