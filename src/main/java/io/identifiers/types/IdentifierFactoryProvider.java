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
                    TypeTemplates.forString, ListTypeTemplates.forStringList, MapTypeTemplates.forStringMap);
            case BOOLEAN:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forBoolean, ListTypeTemplates.forBooleanList, MapTypeTemplates.forBooleanMap);
            case INTEGER:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forInteger, ListTypeTemplates.forIntegerList, MapTypeTemplates.forIntegerMap);
            case FLOAT:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forFloat, ListTypeTemplates.forFloatList, MapTypeTemplates.forFloatMap);
            case LONG:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forLong, ListTypeTemplates.forLongList, MapTypeTemplates.forLongMap);
            case BYTES:
                return new ImmutableIdentifierFactory(
                    TypeTemplates.forBytes, ListTypeTemplates.forBytesList, MapTypeTemplates.forBytesMap);
            default:
                throw new IllegalArgumentException(String.format("No factory for %s", type));
        }
    }
}
