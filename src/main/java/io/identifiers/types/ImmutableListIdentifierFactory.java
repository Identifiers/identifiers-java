package io.identifiers.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.identifiers.ListIdentifier;
import io.identifiers.ListIdentifierFactory;

final class ImmutableListIdentifierFactory<T> implements ListIdentifierFactory<T> {

    private final TypeTemplate<List<T>> listTypeTemplate;

    ImmutableListIdentifierFactory(TypeTemplate<List<T>> listTypeTemplate) {
        this.listTypeTemplate = listTypeTemplate;
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
        return instantiateListIdentifier(copied);
    }

    @Override
    public final Collector<T, ?, ListIdentifier<T>> toListIdentifier() {
        return Collectors.collectingAndThen(Collectors.toList(), this::instantiateListIdentifier);
    }

    private ListIdentifier<T> createSizedListIdentifier(Iterator<T> values, int size) {
        List<T> copied = ListSupport.copyValuesIntoList(values, size);
        return instantiateListIdentifier(copied);
    }

    private ListIdentifier<T> instantiateListIdentifier(List<T> copied) {
        return new ImmutableListIdentifier<>(listTypeTemplate, copied);
    }

    @Override
    public String toString() {
        return String.format("«%s» ID factory", listTypeTemplate.type());
    }
}
