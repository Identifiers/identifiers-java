package io.identifiers;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Create typed Map Identifier instances.
 *
 * @param <T> the value type of the identifiers in the map
 */
public interface MapIdentifierFactory<T> {

    MapIdentifier<T> createMap(Map<String, T> valueMap);

    MapIdentifier<T> createMap(Iterator<Map.Entry<String, T>> entries);

    Collector<T, ?, MapIdentifier<T>> toMapIdentifier(Function<T, String> keyFunction);
}