package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.SingleIdentifierFactory;

final class ImmutableIdentifierFactory<T> implements SingleIdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;

    ImmutableIdentifierFactory(TypeTemplate<T> typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    @Override
    public final Identifier<T> create(T value) {
        return new ImmutableIdentifier<>(typeTemplate, value);
    }

    @Override
    public String toString() {
        return String.format("«%s» ID factory", typeTemplate.type());
    }
}
