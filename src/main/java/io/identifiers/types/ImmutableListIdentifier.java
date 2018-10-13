package io.identifiers.types;

import java.util.List;

import io.identifiers.ListIdentifier;

final class ImmutableListIdentifier<T> extends AbstractIdentifierWithTemplate<List<T>> implements ListIdentifier<T> {

    ImmutableListIdentifier(TypeTemplate<List<T>> typeTemplate, List<T> values) {
        super(typeTemplate, values);
    }
}
