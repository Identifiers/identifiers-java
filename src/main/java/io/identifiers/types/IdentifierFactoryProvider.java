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
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forString, ListTypeTemplates.forStringList);
            case BOOLEAN:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forBoolean, ListTypeTemplates.forBooleanList);
            case INTEGER:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forInteger, ListTypeTemplates.forIntegerList);
            case FLOAT:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forFloat, ListTypeTemplates.forFloatList);
            case LONG:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forLong, ListTypeTemplates.forLongList);
            case BYTES:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forBytes, ListTypeTemplates.forBytesList);
            default:
                throw new IllegalArgumentException(String.format("No factory for %s", type));
        }
    }
}
