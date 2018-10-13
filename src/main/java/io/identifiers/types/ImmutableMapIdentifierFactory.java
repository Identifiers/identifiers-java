package io.identifiers.types;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.identifiers.MapIdentifier;
import io.identifiers.MapIdentifierFactory;

final class ImmutableMapIdentifierFactory<T> implements MapIdentifierFactory<T> {

    private final TypeTemplate<Map<String, T>> mapTypeTemplate;

    ImmutableMapIdentifierFactory(final TypeTemplate<Map<String, T>> mapTypeTemplate) {
        this.mapTypeTemplate = mapTypeTemplate;
    }

    @Override
    public final MapIdentifier<T> createMap(Map<String, T> valueMap) {
        SortedMap<String, T> copied = MapSupport.copyToSortedMap(valueMap);
        return instantiateMapIdentifier(copied);
    }

    @Override
    public final MapIdentifier<T> createMap(Iterator<Map.Entry<String, T>> entries) {
        SortedMap<String, T> copied = MapSupport.copyToSortedMap(entries);
        return instantiateMapIdentifier(copied);
    }

    @Override
    public final Collector<T, ?, MapIdentifier<T>> toMapIdentifier(Function<T, String> keyƒ) {
        return Collectors.collectingAndThen(
            Collectors.toMap(keyƒ, Function.<T>identity(), (e1, e2) -> e1, TreeMap::new),
            this::instantiateMapIdentifier);
    }

    private MapIdentifier<T> instantiateMapIdentifier(Map<String, T> valueMap) {
        return new ImmutableMapIdentifier<>(mapTypeTemplate, valueMap);
    }

    @Override
    public String toString() {
        return String.format("«%s» ID factory", mapTypeTemplate.type());
    }
}
