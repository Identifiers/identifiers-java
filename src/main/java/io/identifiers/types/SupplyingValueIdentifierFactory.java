package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierFactory;

public class SupplyingValueIdentifierFactory<T> implements IdentifierFactory<T> {

    private final TypeTemplate<T> typeTemplate;


    public SupplyingValueIdentifierFactory(TypeTemplate<T> typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    @Override
    public Identifier<T> create(T value) {
        return new SupplyingValueIdentifier<>(typeTemplate, typeTemplate.valueSupplier(value));
    }
}
