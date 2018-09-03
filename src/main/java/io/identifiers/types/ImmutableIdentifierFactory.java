package io.identifiers.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;
import io.identifiers.ListIdentifier;

class ImmutableIdentifierFactory<T> implements IdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;
    private final TypeTemplate<List<T>> listTypeTemplate;


    ImmutableIdentifierFactory(TypeTemplate<T> typeTemplate, TypeTemplate<List<T>> listTypeTemplate) {
        this.typeTemplate = typeTemplate;
        this.listTypeTemplate = listTypeTemplate;
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
}
