package io.identifiers.types;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;

import io.identifiers.IdentifierType;
import io.identifiers.MapIdentifier;

final class ImmutableMapIdentifier<T> implements MapIdentifier<T> {

    private final MapTypeTemplate<T> typeTemplate;
    private final SortedMap<String, T> valueMap;

    ImmutableMapIdentifier(MapTypeTemplate<T> typeTemplate, SortedMap<String, T> valueMap) {
        this.typeTemplate = typeTemplate;
        // expects values list to be copied from source list by factory
        this.valueMap = Collections.unmodifiableSortedMap(typeTemplate.initialValueMap(valueMap));
    }

    @Override
    public IdentifierType type() {
        return typeTemplate.type();
    }

    @Override
    public Map<String, T> value() {
        return typeTemplate.value(valueMap);
    }

    @Override
    public String toDataString() {
        return typeTemplate.toDataString(valueMap);
    }

    @Override
    public String toHumanString() {
        return typeTemplate.toHumanString(valueMap);
    }

    @Override
    public String toString() {
        return String.format("ID«%s»:%s", type(), typeTemplate.valueString(valueMap));
    }

    @Override
    public int hashCode() {
        return typeTemplate.valueHashCode(valueMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableMapIdentifier) {
            ImmutableMapIdentifier<T> otherMapId = (ImmutableMapIdentifier<T>) obj;
            return type() == otherMapId.type()
                && typeTemplate.valuesEqual(valueMap, otherMapId.valueMap);
        }
        return false;
    }
}
