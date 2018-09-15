package io.identifiers.types;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;

import io.identifiers.IdentifierType;
import io.identifiers.MapIdentifier;
import io.identifiers.TypeCodeModifiers;

final class ImmutableMapIdentifier<T> implements MapIdentifier<T> {

    private final TypeTemplate<SortedMap<String, T>> typeTemplate;
    private final SortedMap<String, T> values;

    ImmutableMapIdentifier(TypeTemplate<SortedMap<String, T>> typeTemplate, SortedMap<String, T> values) {
        this.typeTemplate = typeTemplate;
        assert TypeCodeModifiers.MAP_TYPE_CODE == (typeTemplate.type().code() & TypeCodeModifiers.MAP_TYPE_CODE)
            : String.format("Not a Map type: %s", typeTemplate);

        // expects values list to be copied from source list by factory
        this.values = typeTemplate.isValueMutable()
            ? typeTemplate.value(values)
            : Collections.unmodifiableSortedMap(values);
    }

    @Override
    public IdentifierType type() {
        return typeTemplate.type();
    }

    @Override
    public Map<String, T> value() {
        return typeTemplate.value(values);
    }

    @Override
    public String toDataString() {
        return typeTemplate.toDataString(values);
    }

    @Override
    public String toHumanString() {
        return typeTemplate.toHumanString(values);
    }

    @Override
    public String toString() {
        return String.format("ID«%s»:%s",
            type().name().toLowerCase().replace('_', '-'), // kebab-case
            typeTemplate.valueString(values));
    }

    @Override
    public int hashCode() {
        return typeTemplate.valueHashCode(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MapIdentifier && ((MapIdentifier) obj).type() == type()) {
            SortedMap<String, T> otherValues = (SortedMap<String, T>) ((MapIdentifier<T>) obj).value();
            return typeTemplate.valuesEqual(values, otherValues);
        }
        return false;
    }
}
