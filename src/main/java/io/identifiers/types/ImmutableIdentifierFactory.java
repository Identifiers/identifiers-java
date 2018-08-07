package io.identifiers.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;
import io.identifiers.ListIdentifier;

public class ImmutableIdentifierFactory<T> implements IdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;
    private final TypeTemplate<List<T>> listTypeTemplate;


    ImmutableIdentifierFactory(TypeTemplate<T> typeTemplate, TypeTemplate<List<T>> listTypeTemplate) {
        this.typeTemplate = typeTemplate;
        this.listTypeTemplate = listTypeTemplate;
    }

    @Override
    public Identifier<T> create(T value) {
        return new ImmutableIdentifier<>(typeTemplate, value);
    }


    @Override
    public ListIdentifier<T> createList(Iterable<T> values) {
      return createList(values.iterator());
    }

    @Override
    public ListIdentifier<T> createList(Collection<T> values) {
        List<T> list = new ArrayList<>(values);
        return toImmutableListIdentifier(list);
    }

    @Override
    public ListIdentifier<T> createList(Iterator<T> values) {
        List<T> list = new ArrayList<>();
        while (values.hasNext()) {
            list.add(values.next());
        }
        return toImmutableListIdentifier(list);
    }

    @Override
    public ListIdentifier<T> createList(T... values) {
        List<T> list = Arrays.asList(values.clone());
        return toImmutableListIdentifier(list);
    }

    @Override
    public Collector<T, ?, ListIdentifier<T>> toListIdentifier() {
        return Collector.of(
            ArrayList::new,
            List::add,
            listCombiner(),
            this::toImmutableListIdentifier
        );
    }

    private BinaryOperator<List<T>> listCombiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    private ListIdentifier<T> toImmutableListIdentifier(List<T> list) {
        return new ImmutableListIdentifier<>(listTypeTemplate, list);
    }
}
