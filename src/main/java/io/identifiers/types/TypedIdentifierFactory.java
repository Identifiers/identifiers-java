package io.identifiers.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;
import io.identifiers.ListIdentifier;
import io.identifiers.ListIdentifierFactory;
import io.identifiers.MapIdentifier;
import io.identifiers.MapIdentifierFactory;
import io.identifiers.SingleIdentifierFactory;

final class TypedIdentifierFactory<T> implements IdentifierFactory<T> {

    private final SingleIdentifierFactory<T> singleFactory;
    private final ListIdentifierFactory<T> listFactory;
    private final MapIdentifierFactory<T> mapFactory;

    TypedIdentifierFactory(
            SingleIdentifierFactory<T> singleFactory,
            ListIdentifierFactory<T> listFactory,
            MapIdentifierFactory<T> mapFactory) {

        this.listFactory = listFactory;
        this.mapFactory = mapFactory;
        this.singleFactory = singleFactory;
    }

    @Override
    public Identifier<T> create(T value) {
        return singleFactory.create(value);
    }

    @Override
    public ListIdentifier<T> createList(Collection<T> values) {
        return listFactory.createList(values);
    }

    @Override
    public ListIdentifier<T> createList(Iterator<T> values) {
        return listFactory.createList(values);
    }

    @Override
    public ListIdentifier<T> createList(T... values) {
        return listFactory.createList(values);
    }

    @Override
    public Collector<T, ?, ListIdentifier<T>> toListIdentifier() {
        return listFactory.toListIdentifier();
    }

    @Override
    public MapIdentifier<T> createMap(Map<String, T> valueMap) {
        return mapFactory.createMap(valueMap);
    }

    @Override
    public MapIdentifier<T> createMap(Iterator<Map.Entry<String, T>> entries) {
        return mapFactory.createMap(entries);
    }

    @Override
    public Collector<T, ?, MapIdentifier<T>> toMapIdentifier(Function<T, String> keyƒ) {
        return mapFactory.toMapIdentifier(keyƒ);
    }
}
