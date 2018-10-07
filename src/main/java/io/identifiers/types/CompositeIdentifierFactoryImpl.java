package io.identifiers.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import io.identifiers.CompositeIdentifierFactory;
import io.identifiers.Identifier;
import io.identifiers.ListIdentifier;
import io.identifiers.ListIdentifierFactory;
import io.identifiers.MapIdentifier;
import io.identifiers.MapIdentifierFactory;

final class CompositeIdentifierFactoryImpl implements CompositeIdentifierFactory {

    private final ListIdentifierFactory<Identifier<?>> listFactory;
    private final MapIdentifierFactory<Identifier<?>> mapFactory;

    CompositeIdentifierFactoryImpl(
            ListIdentifierFactory<Identifier<?>> listFactory,
            MapIdentifierFactory<Identifier<?>> mapFactory) {
        this.listFactory = listFactory;
        this.mapFactory = mapFactory;
    }

    @Override
    public final ListIdentifier<Identifier<?>> createList(Collection<Identifier<?>> values) {
        return listFactory.createList(values);
    }

    @Override
    public final ListIdentifier<Identifier<?>> createList(Iterator<Identifier<?>> values) {
        return listFactory.createList(values);
    }

    @Override
    public final ListIdentifier<Identifier<?>> createList(Identifier<?>... values) {
        return listFactory.createList(values);
    }

    @Override
    public final Collector<Identifier<?>, ?, ListIdentifier<Identifier<?>>> toListIdentifier() {
        return listFactory.toListIdentifier();
    }

    @Override
    public final MapIdentifier<Identifier<?>> createMap(Map<String, Identifier<?>> valueMap) {
        return mapFactory.createMap(valueMap);
    }

    @Override
    public final MapIdentifier<Identifier<?>> createMap(Iterator<Map.Entry<String, Identifier<?>>> entries) {
        return mapFactory.createMap(entries);
    }

    @Override
    public final Collector<Identifier<?>, ?, MapIdentifier<Identifier<?>>> toMapIdentifier(Function<Identifier<?>, String> keyƒ) {
        return mapFactory.toMapIdentifier(keyƒ);
    }
}
