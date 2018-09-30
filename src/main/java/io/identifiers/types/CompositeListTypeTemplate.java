package io.identifiers.types;

import java.util.List;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

final class CompositeListTypeTemplate implements ListTypeTemplate<Identifier<?>> {

    private final IdentifierEncoder<List<Identifier<?>>> listEncoder;

    CompositeListTypeTemplate(final IdentifierEncoder<List<Identifier<?>>> listEncoder) {
        this.listEncoder = listEncoder;
    }

    @Override
    public IdentifierType type() {
        return IdentifierType.COMPOSITE_LIST;
    }

    @Override
    public boolean isValueMutable() {
        return false;
    }

    @Override
    public List<Identifier<?>> initialValues(final List<Identifier<?>> values) {
        return values;
    }

    @Override
    public List<Identifier<?>> value(List<Identifier<?>> values) {
        return values;
    }

    @Override
    public String toDataString(List<Identifier<?>> values) {
        return listEncoder.toDataString(values);
    }

    @Override
    public String toHumanString(List<Identifier<?>> values) {
        return listEncoder.toHumanString(values);
    }

    @Override
    public String valueString(List<Identifier<?>> values) {
        return values.toString();
    }

    @Override
    public int valueHashCode(List<Identifier<?>> values) {
        return values.hashCode();
    }

    @Override
    public boolean valuesEqual(List<Identifier<?>> list1, List<Identifier<?>> list2) {
        return list1.equals(list2);
    }
}
