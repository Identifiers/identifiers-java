package io.identifiers;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Create typed Map Identifier instances.
 *
 * @param <T> the value type of the identifiers in the map.
 */
public interface MapIdentifierFactory<T> {

    /**
     * Create from a map of values.
     *
     * @param valueMap the mapped values.
     * @return an identifier of the map.
     */
    MapIdentifier<T> createMap(Map<String, T> valueMap);

    /**
     * Create a map from an iterator of value entries.
     *
     * @param entries the value entries.
     * @return an identifier of the entries.
     */
    MapIdentifier<T> createMap(Iterator<Map.Entry<String, T>> entries);

    /**
     * A Java Stream collector for mapping values into a map identifier.
     *
     * @param keyƒ a Function that provides a value for the given key.
     * @return A Collector suitable for use with {@link java.util.stream.Stream#collect(Collector)}.
     */
    Collector<T, ?, MapIdentifier<T>> toMapIdentifier(Function<T, String> keyƒ);
}