package io.identifiers.types;

import java.util.Map;

import io.identifiers.MapIdentifier;

final class ImmutableMapIdentifier<T> extends AbstractIdentifierWithTemplate<Map<String, T>> implements MapIdentifier<T> {

    ImmutableMapIdentifier(TypeTemplate<Map<String, T>> mapTypeTemplate, Map<String, T> valueMap) {
        super(mapTypeTemplate, valueMap);
    }
}
