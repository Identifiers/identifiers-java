package io.identifiers.types;

import io.identifiers.IdentifierType;

final class ListTypeTemplates {

    private ListTypeTemplates() {
        // static class
    }

    static ListTypeTemplate<String> forStringList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.STRING_LIST, ValueCodecs.stringCodec),
            TypeTemplates.forString);

    static ListTypeTemplate<Boolean> forBooleanList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.BOOLEAN_LIST, ValueCodecs.booleanCodec),
            TypeTemplates.forBoolean);

    static ListTypeTemplate<Integer> forIntegerList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.INTEGER_LIST, ValueCodecs.integerCodec),
            TypeTemplates.forInteger);

    static ListTypeTemplate<Float> forFloatList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.FLOAT_LIST, ValueCodecs.floatCodec),
            TypeTemplates.forFloat);

    static ListTypeTemplate<Long> forLongList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.LONG_LIST, ValueCodecs.longCodec),
            TypeTemplates.forLong);

    static ListTypeTemplate<byte[]> forBytesList = new ListTypeTemplate<>(
            new ListIdentifierEncoder<>(IdentifierType.BYTES_LIST, ValueCodecs.bytesCodec),
            TypeTemplates.forBytes);
}
