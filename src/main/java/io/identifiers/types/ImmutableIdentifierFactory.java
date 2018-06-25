package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;

public class ImmutableIdentifierFactory<T> implements IdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;

    public ImmutableIdentifierFactory(TypeTemplate<T> typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    @Override
    public Identifier<T> create(T value) {
        return new ImmutableIdentifier<>(typeTemplate, value);
    }
}
