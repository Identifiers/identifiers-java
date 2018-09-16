package io.identifiers.types;

import io.identifiers.IdentifierType;

final class MapTypeTemplates {

    private MapTypeTemplates() {
        // static class
    }

    static MapTypeTemplate<String> forStringMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.STRING_MAP, ValueCodecs.stringCodec),
            TypeTemplates.forString);

    static MapTypeTemplate<Boolean> forBooleanMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.BOOLEAN_MAP, ValueCodecs.booleanCodec),
            TypeTemplates.forBoolean);

    static MapTypeTemplate<Integer> forIntegerMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.INTEGER_MAP, ValueCodecs.integerCodec),
            TypeTemplates.forInteger);

    static MapTypeTemplate<Float> forFloatMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.FLOAT_MAP, ValueCodecs.floatCodec),
            TypeTemplates.forFloat);

    static MapTypeTemplate<Long> forLongMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.LONG_MAP, ValueCodecs.longCodec),
            TypeTemplates.forLong);

    static MapTypeTemplate<byte[]> forBytesMap = new MapTypeTemplate<>(
            new MapIdentifierEncoder<>(IdentifierType.BYTES_MAP, ValueCodecs.bytesCodec),
            TypeTemplates.forBytes);
}
