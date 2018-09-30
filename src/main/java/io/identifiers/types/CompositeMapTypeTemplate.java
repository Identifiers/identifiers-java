package io.identifiers.types;

import java.util.SortedMap;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

/**
 * Composite identifiers do not have to worry about the mutability of their identifier
 * contents because they manage their own mutability. The user will discover that attempting
 * to modify a mutable identifier value, like byte[], will not affect either that identifier
 * or the composite identifier that contains it.
 */
final class CompositeMapTypeTemplate implements MapTypeTemplate<Identifier<?>> {

    private final IdentifierEncoder<SortedMap<String, Identifier<?>>> mapEncoder;

    CompositeMapTypeTemplate(IdentifierEncoder<SortedMap<String, Identifier<?>>> mapEncoder) {
        this.mapEncoder = mapEncoder;
    }

    @Override
    public IdentifierType type() {
        return IdentifierType.COMPOSITE_MAP;
    }

    @Override
    public boolean isValueMutable() {
        return false;
    }

    @Override
    public SortedMap<String, Identifier<?>> initialValueMap(final SortedMap<String, Identifier<?>> valueMap) {
        return valueMap;
    }

    @Override
    public SortedMap<String, Identifier<?>> value(SortedMap<String, Identifier<?>> valueMap) {
        return valueMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toDataString(SortedMap<String, Identifier<?>> valueMap) {
        return mapEncoder.toDataString(valueMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toHumanString(SortedMap<String, Identifier<?>> valueMap) {
        return mapEncoder.toHumanString(valueMap);
    }

    @Override
    public String valueString(SortedMap<String, Identifier<?>> valueMap) {
        return valueMap.toString();
    }

    @Override
    public int valueHashCode(SortedMap<String, Identifier<?>> valueMap) {
        return valueMap.hashCode();
    }

    @Override
    public boolean valuesEqual(SortedMap<String, Identifier<?>> map1, SortedMap<String, Identifier<?>> map2) {
        return map1.equals(map2);
    }
}
