package io.identifiers.types;

import io.identifiers.IdentifierFactory;
import io.identifiers.IdentifierType;

public final class IdentifierFactoryProvider {

    private IdentifierFactoryProvider() {
        // static class
    }

    @SuppressWarnings("unchecked")
    public static <T> IdentifierFactory<T> createFactory(IdentifierType type) {

        switch (type) {
            case STRING:
                return new ImmutableIdentifierFactory(TypeTemplates.forString);
            case BOOLEAN:
                return new ImmutableIdentifierFactory(TypeTemplates.forBoolean);
            case INTEGER:
                return new ImmutableIdentifierFactory(TypeTemplates.forInteger);
            case FLOAT:
                return new ImmutableIdentifierFactory(TypeTemplates.forFloat);
            case LONG:
                return new ImmutableIdentifierFactory(TypeTemplates.forLong);
            case BYTES:
                return new SupplyingValueIdentifierFactory(TypeTemplates.forBytes);
            default:
                throw new IllegalArgumentException(String.format("No factory for %s", type));
        }
    }
}
