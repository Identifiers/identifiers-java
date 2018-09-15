package io.identifiers.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;
import io.identifiers.ListIdentifier;
import io.identifiers.MapIdentifier;

class ImmutableIdentifierFactory<T> implements IdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;
    private final TypeTemplate<List<T>> listTypeTemplate;
    private final TypeTemplate<SortedMap<String, T>> mapTypeTemplate;


    ImmutableIdentifierFactory(TypeTemplate<T> typeTemplate, TypeTemplate<List<T>> listTypeTemplate, TypeTemplate<SortedMap<String, T>> mapTypeTemplate) {
        this.typeTemplate = typeTemplate;
        this.listTypeTemplate = listTypeTemplate;
        this.mapTypeTemplate = mapTypeTemplate;
    }

    @Override
    public final Identifier<T> create(T value) {
        return new ImmutableIdentifier<>(typeTemplate, value);
    }

    @Override
    public final ListIdentifier<T> createList(Collection<T> values) {
        return createSizedListIdentifier(values.iterator(), values.size());
    }

    @SafeVarargs
    @Override
    public final ListIdentifier<T> createList(T... values) {
        List<T> list = Arrays.asList(values);
        return createSizedListIdentifier(list.iterator(), values.length);
    }

    @Override
    public final ListIdentifier<T> createList(Iterator<T> values) {
        List<T> copied = ListSupport.copyValuesIntoList(values);
        return toImmutableListIdentifier(copied);
    }

    @Override
    public final Collector<T, ?, ListIdentifier<T>> toListIdentifier() {
        return Collectors.collectingAndThen(Collectors.toList(), this::toImmutableListIdentifier);
    }

    private ListIdentifier<T> createSizedListIdentifier(Iterator<T> values, int size) {
        List<T> copied = ListSupport.copyValuesIntoList(values, size);
        return toImmutableListIdentifier(copied);
    }

    private ListIdentifier<T> toImmutableListIdentifier(List<T> values) {
        return new ImmutableListIdentifier<>(listTypeTemplate, values);
    }

    @Override
    public MapIdentifier<T> createMap(Map<String, T> values) {
        SortedMap<String, T> copied = MapSupport.copyToSortedMap(values);
        return toImmutableMapIdentifier(copied);
    }

    @Override
    public MapIdentifier<T> createMap(Iterator<Map.Entry<String, T>> entries) {
        SortedMap<String, T> copied = MapSupport.copyToSortedMap(entries);
        return toImmutableMapIdentifier(copied);
    }

    @Override
    public Collector<T, ?, MapIdentifier<T>> toMapIdentifier(Function<T, String> keyFunction) {
        return Collectors.collectingAndThen(
            Collectors.toMap(keyFunction, Function.<T>identity(), (e1, e2) -> e1, TreeMap::new),
            this::toImmutableMapIdentifier);
    }

    private MapIdentifier<T> toImmutableMapIdentifier(SortedMap<String, T> valueMap) {
        return new ImmutableMapIdentifier<>(mapTypeTemplate, valueMap);
    }
}
